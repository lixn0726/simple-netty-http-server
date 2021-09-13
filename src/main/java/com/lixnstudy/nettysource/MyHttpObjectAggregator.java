package com.lixnstudy.nettysource;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.MessageAggregator;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @Author lixn
 * @ClassName MyHttpObjectAggregator
 * @CreateDate 2021/9/13
 * @Description
 */
public class MyHttpObjectAggregator extends MessageAggregator<HttpObject, HttpMessage, HttpContent, FullHttpMessage> {
    //////////////////////////////////////////// ---- 泛型对应：I , S  , C extends ByteBufHolder, O extends ByteBufHolder
    /*
    HttpObject -> I
    HttpMessage -> S
    HttpContent -> C extends ByteBufHolder
    FullHttpMessage -> O extends ByteBufHolder
     */
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(HttpObjectAggregator.class);
    private static final FullHttpResponse CONTINUE;// 接受100-continue，响应状态码100
    private static final FullHttpResponse EXPECTATION_FAILED;// 不接受，响应状态码417 不支持
    private static final FullHttpResponse TOO_LARGE;// 不接受，响应状态码413 消息题太大，不关闭连接
    private final boolean closeOnExpectationFailed;

    public MyHttpObjectAggregator(int maxContentLength) {
        this(maxContentLength, false);
    }

    public MyHttpObjectAggregator(int maxContentLength, boolean closeOnExpectationFailed) {
        super(maxContentLength);
        this.closeOnExpectationFailed = closeOnExpectationFailed;
    }

    protected boolean isStartMessage(HttpObject msg) throws Exception {
        return msg instanceof HttpMessage;
    }

    protected boolean isContentMessage(HttpObject msg) throws Exception {
        return msg instanceof HttpContent;
    }

    protected boolean isLastContentMessage(HttpContent msg) throws Exception {
        return msg instanceof LastHttpContent;
    }

    protected boolean isAggregated(HttpObject msg) throws Exception {
        return msg instanceof FullHttpMessage;
    }

    protected boolean isContentLengthInvalid(HttpMessage start, int maxContentLength) {
        return HttpUtil.getContentLength(start, -1L) > (long)maxContentLength;
    }

    protected Object newContinueResponse(HttpMessage start, int maxContentLength, ChannelPipeline pipeline) {
        if (HttpUtil.is100ContinueExpected(start)) {
            if (HttpUtil.getContentLength(start, -1L) <= (long)maxContentLength) {
                return CONTINUE.retainedDuplicate();
            } else {
                pipeline.fireUserEventTriggered(HttpExpectationFailedEvent.INSTANCE);
                return EXPECTATION_FAILED.retainedDuplicate();
            }
        } else {
            return null;
        }
    }

    protected boolean closeAfterContinueResponse(Object msg) {
        return this.closeOnExpectationFailed && this.ignoreContentAfterContinueResponse(msg);
    }

    protected boolean ignoreContentAfterContinueResponse(Object msg) {
        return msg instanceof HttpResponse && ((HttpResponse)msg).status().code() == HttpResponseStatus.EXPECTATION_FAILED.code();
    }

    protected FullHttpMessage beginAggregation(HttpMessage start, ByteBuf content) throws Exception {
        assert !(start instanceof FullHttpMessage);

        HttpUtil.setTransferEncodingChunked(start, false);
        Object ret;
        if (start instanceof HttpRequest) {
            ret = new AggregatedFullHttpRequest((HttpRequest)start, content, (HttpHeaders)null);
        } else {
            if (!(start instanceof HttpResponse)) {
                throw new Error();
            }

            ret = new AggregatedFullHttpResponse((HttpResponse)start, content, (HttpHeaders)null);
        }

        return (FullHttpMessage)ret;
    }

    protected void aggregate(FullHttpMessage aggregated, HttpContent content) throws Exception {
        if (content instanceof LastHttpContent) {
            ((AggregatedFullHttpMessage)aggregated).setTrailingHeaders(((LastHttpContent)content).trailingHeaders());
        }

    }

    protected void finishAggregation(FullHttpMessage aggregated) throws Exception {
        if (!HttpUtil.isContentLengthSet(aggregated)) {
            aggregated.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(aggregated.content().readableBytes()));
        }

    }

    protected void handleOversizedMessage(final ChannelHandlerContext ctx, HttpMessage oversized) throws Exception {
        if (!(oversized instanceof HttpRequest)) {
            if (oversized instanceof HttpResponse) {
                ctx.close();
                throw new TooLongFrameException("Response entity too large: " + oversized);
            } else {
                throw new IllegalStateException();
            }
        } else {
            ChannelFuture future = ctx.writeAndFlush(TOO_LARGE.retainedDuplicate()).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        logger.debug("Failed to send a 413 Request Entity Too Large.", future.cause());
                        ctx.close();
                    }

                }
            });
            if (oversized instanceof FullHttpMessage || !HttpUtil.is100ContinueExpected(oversized) && !HttpUtil.isKeepAlive(oversized)) {
                future.addListener(ChannelFutureListener.CLOSE);
            }

            HttpObjectDecoder decoder = (HttpObjectDecoder)ctx.pipeline().get(HttpObjectDecoder.class);
            if (decoder != null) {
                decoder.reset();
            }

        }
    }

    static {
        CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
        EXPECTATION_FAILED = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.EXPECTATION_FAILED, Unpooled.EMPTY_BUFFER);
        TOO_LARGE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);
        EXPECTATION_FAILED.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        TOO_LARGE.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
    }

    private static final class AggregatedFullHttpResponse extends AggregatedFullHttpMessage implements FullHttpResponse {
        AggregatedFullHttpResponse(HttpResponse message, ByteBuf content, HttpHeaders trailingHeaders) {
            super(message, content, trailingHeaders);
        }

        public FullHttpResponse copy() {
            return this.replace(this.content().copy());
        }

        public FullHttpResponse duplicate() {
            return this.replace(this.content().duplicate());
        }

        public FullHttpResponse retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }

        public FullHttpResponse replace(ByteBuf content) {
            DefaultFullHttpResponse dup = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), content);
            dup.headers().set(this.headers());
            dup.trailingHeaders().set(this.trailingHeaders());
            return dup;
        }

        public FullHttpResponse setStatus(HttpResponseStatus status) {
            ((HttpResponse)this.message).setStatus(status);
            return this;
        }

        public HttpResponseStatus getStatus() {
            return ((HttpResponse)this.message).status();
        }

        public HttpResponseStatus status() {
            return this.getStatus();
        }

        public FullHttpResponse setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        public FullHttpResponse retain(int increment) {
            super.retain(increment);
            return this;
        }

        public FullHttpResponse retain() {
            super.retain();
            return this;
        }

        public FullHttpResponse touch(Object hint) {
            super.touch(hint);
            return this;
        }

        public FullHttpResponse touch() {
            super.touch();
            return this;
        }

//        public String toString() {
//            return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
//        }
    }

    private static final class AggregatedFullHttpRequest extends AggregatedFullHttpMessage implements FullHttpRequest {
        AggregatedFullHttpRequest(HttpRequest request, ByteBuf content, HttpHeaders trailingHeaders) {
            super(request, content, trailingHeaders);
        }

        public FullHttpRequest copy() {
            return this.replace(this.content().copy());
        }

        public FullHttpRequest duplicate() {
            return this.replace(this.content().duplicate());
        }

        public FullHttpRequest retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }

        public FullHttpRequest replace(ByteBuf content) {
            DefaultFullHttpRequest dup = new DefaultFullHttpRequest(this.protocolVersion(), this.method(), this.uri(), content);
            dup.headers().set(this.headers());
            dup.trailingHeaders().set(this.trailingHeaders());
            return dup;
        }

        public FullHttpRequest retain(int increment) {
            super.retain(increment);
            return this;
        }

        public FullHttpRequest retain() {
            super.retain();
            return this;
        }

        public FullHttpRequest touch() {
            super.touch();
            return this;
        }

        public FullHttpRequest touch(Object hint) {
            super.touch(hint);
            return this;
        }

        public FullHttpRequest setMethod(HttpMethod method) {
            ((HttpRequest)this.message).setMethod(method);
            return this;
        }

        public FullHttpRequest setUri(String uri) {
            ((HttpRequest)this.message).setUri(uri);
            return this;
        }

        public HttpMethod getMethod() {
            return ((HttpRequest)this.message).method();
        }

        public String getUri() {
            return ((HttpRequest)this.message).uri();
        }

        public HttpMethod method() {
            return this.getMethod();
        }

        public String uri() {
            return this.getUri();
        }

        public FullHttpRequest setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

//        public String toString() {
//            return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
//        }
    }

    private abstract static class AggregatedFullHttpMessage implements FullHttpMessage {
        protected final HttpMessage message;
        private final ByteBuf content;
        private HttpHeaders trailingHeaders;

        AggregatedFullHttpMessage(HttpMessage message, ByteBuf content, HttpHeaders trailingHeaders) {
            this.message = message;
            this.content = content;
            this.trailingHeaders = trailingHeaders;
        }

        public HttpHeaders trailingHeaders() {
            HttpHeaders trailingHeaders = this.trailingHeaders;
            return (HttpHeaders)(trailingHeaders == null ? EmptyHttpHeaders.INSTANCE : trailingHeaders);
        }

        void setTrailingHeaders(HttpHeaders trailingHeaders) {
            this.trailingHeaders = trailingHeaders;
        }

        public HttpVersion getProtocolVersion() {
            return this.message.protocolVersion();
        }

        public HttpVersion protocolVersion() {
            return this.message.protocolVersion();
        }

        public FullHttpMessage setProtocolVersion(HttpVersion version) {
            this.message.setProtocolVersion(version);
            return this;
        }

        public HttpHeaders headers() {
            return this.message.headers();
        }

        public DecoderResult decoderResult() {
            return this.message.decoderResult();
        }

        public DecoderResult getDecoderResult() {
            return this.message.decoderResult();
        }

        public void setDecoderResult(DecoderResult result) {
            this.message.setDecoderResult(result);
        }

        public ByteBuf content() {
            return this.content;
        }

        public int refCnt() {
            return this.content.refCnt();
        }

        public FullHttpMessage retain() {
            this.content.retain();
            return this;
        }

        public FullHttpMessage retain(int increment) {
            this.content.retain(increment);
            return this;
        }

        public FullHttpMessage touch(Object hint) {
            this.content.touch(hint);
            return this;
        }

        public FullHttpMessage touch() {
            this.content.touch();
            return this;
        }

        public boolean release() {
            return this.content.release();
        }

        public boolean release(int decrement) {
            return this.content.release(decrement);
        }

        public abstract FullHttpMessage copy();

        public abstract FullHttpMessage duplicate();

        public abstract FullHttpMessage retainedDuplicate();
    }
}
