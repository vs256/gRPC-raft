package route;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
// @javax.annotation.Generated(
//     value = "by gRPC proto compiler (version 1.44.1)",
//     comments = "Source: route.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RouteServiceGrpc {

  private RouteServiceGrpc() {}

  public static final String SERVICE_NAME = "route.RouteService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<route.Route,
      route.Route> getRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "request",
      requestType = route.Route.class,
      responseType = route.Route.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<route.Route,
      route.Route> getRequestMethod() {
    io.grpc.MethodDescriptor<route.Route, route.Route> getRequestMethod;
    if ((getRequestMethod = RouteServiceGrpc.getRequestMethod) == null) {
      synchronized (RouteServiceGrpc.class) {
        if ((getRequestMethod = RouteServiceGrpc.getRequestMethod) == null) {
          RouteServiceGrpc.getRequestMethod = getRequestMethod =
              io.grpc.MethodDescriptor.<route.Route, route.Route>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "request"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setSchemaDescriptor(new RouteServiceMethodDescriptorSupplier("request"))
              .build();
        }
      }
    }
    return getRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<route.Route,
      route.Route> getNominateRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "nominateRequest",
      requestType = route.Route.class,
      responseType = route.Route.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<route.Route,
      route.Route> getNominateRequestMethod() {
    io.grpc.MethodDescriptor<route.Route, route.Route> getNominateRequestMethod;
    if ((getNominateRequestMethod = RouteServiceGrpc.getNominateRequestMethod) == null) {
      synchronized (RouteServiceGrpc.class) {
        if ((getNominateRequestMethod = RouteServiceGrpc.getNominateRequestMethod) == null) {
          RouteServiceGrpc.getNominateRequestMethod = getNominateRequestMethod =
              io.grpc.MethodDescriptor.<route.Route, route.Route>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "nominateRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setSchemaDescriptor(new RouteServiceMethodDescriptorSupplier("nominateRequest"))
              .build();
        }
      }
    }
    return getNominateRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<route.Route,
      route.Route> getVoteRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "voteRequest",
      requestType = route.Route.class,
      responseType = route.Route.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<route.Route,
      route.Route> getVoteRequestMethod() {
    io.grpc.MethodDescriptor<route.Route, route.Route> getVoteRequestMethod;
    if ((getVoteRequestMethod = RouteServiceGrpc.getVoteRequestMethod) == null) {
      synchronized (RouteServiceGrpc.class) {
        if ((getVoteRequestMethod = RouteServiceGrpc.getVoteRequestMethod) == null) {
          RouteServiceGrpc.getVoteRequestMethod = getVoteRequestMethod =
              io.grpc.MethodDescriptor.<route.Route, route.Route>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "voteRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setSchemaDescriptor(new RouteServiceMethodDescriptorSupplier("voteRequest"))
              .build();
        }
      }
    }
    return getVoteRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<route.Route,
      route.Route> getBiDirectionalRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "biDirectionalRequest",
      requestType = route.Route.class,
      responseType = route.Route.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<route.Route,
      route.Route> getBiDirectionalRequestMethod() {
    io.grpc.MethodDescriptor<route.Route, route.Route> getBiDirectionalRequestMethod;
    if ((getBiDirectionalRequestMethod = RouteServiceGrpc.getBiDirectionalRequestMethod) == null) {
      synchronized (RouteServiceGrpc.class) {
        if ((getBiDirectionalRequestMethod = RouteServiceGrpc.getBiDirectionalRequestMethod) == null) {
          RouteServiceGrpc.getBiDirectionalRequestMethod = getBiDirectionalRequestMethod =
              io.grpc.MethodDescriptor.<route.Route, route.Route>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "biDirectionalRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setSchemaDescriptor(new RouteServiceMethodDescriptorSupplier("biDirectionalRequest"))
              .build();
        }
      }
    }
    return getBiDirectionalRequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RouteServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouteServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouteServiceStub>() {
        @java.lang.Override
        public RouteServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouteServiceStub(channel, callOptions);
        }
      };
    return RouteServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RouteServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouteServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouteServiceBlockingStub>() {
        @java.lang.Override
        public RouteServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouteServiceBlockingStub(channel, callOptions);
        }
      };
    return RouteServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RouteServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RouteServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RouteServiceFutureStub>() {
        @java.lang.Override
        public RouteServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RouteServiceFutureStub(channel, callOptions);
        }
      };
    return RouteServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RouteServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void request(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRequestMethod(), responseObserver);
    }

    /**
     */
    public void nominateRequest(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNominateRequestMethod(), responseObserver);
    }

    /**
     */
    public void voteRequest(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVoteRequestMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<route.Route> biDirectionalRequest(
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getBiDirectionalRequestMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                route.Route,
                route.Route>(
                  this, METHODID_REQUEST)))
          .addMethod(
            getNominateRequestMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                route.Route,
                route.Route>(
                  this, METHODID_NOMINATE_REQUEST)))
          .addMethod(
            getVoteRequestMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                route.Route,
                route.Route>(
                  this, METHODID_VOTE_REQUEST)))
          .addMethod(
            getBiDirectionalRequestMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                route.Route,
                route.Route>(
                  this, METHODID_BI_DIRECTIONAL_REQUEST)))
          .build();
    }
  }

  /**
   */
  public static final class RouteServiceStub extends io.grpc.stub.AbstractAsyncStub<RouteServiceStub> {
    private RouteServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouteServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouteServiceStub(channel, callOptions);
    }

    /**
     */
    public void request(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void nominateRequest(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNominateRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void voteRequest(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVoteRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<route.Route> biDirectionalRequest(
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getBiDirectionalRequestMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class RouteServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<RouteServiceBlockingStub> {
    private RouteServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouteServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouteServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public route.Route request(route.Route request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public route.Route nominateRequest(route.Route request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNominateRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public route.Route voteRequest(route.Route request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVoteRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RouteServiceFutureStub extends io.grpc.stub.AbstractFutureStub<RouteServiceFutureStub> {
    private RouteServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouteServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RouteServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<route.Route> request(
        route.Route request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<route.Route> nominateRequest(
        route.Route request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNominateRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<route.Route> voteRequest(
        route.Route request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVoteRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST = 0;
  private static final int METHODID_NOMINATE_REQUEST = 1;
  private static final int METHODID_VOTE_REQUEST = 2;
  private static final int METHODID_BI_DIRECTIONAL_REQUEST = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RouteServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RouteServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST:
          serviceImpl.request((route.Route) request,
              (io.grpc.stub.StreamObserver<route.Route>) responseObserver);
          break;
        case METHODID_NOMINATE_REQUEST:
          serviceImpl.nominateRequest((route.Route) request,
              (io.grpc.stub.StreamObserver<route.Route>) responseObserver);
          break;
        case METHODID_VOTE_REQUEST:
          serviceImpl.voteRequest((route.Route) request,
              (io.grpc.stub.StreamObserver<route.Route>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BI_DIRECTIONAL_REQUEST:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.biDirectionalRequest(
              (io.grpc.stub.StreamObserver<route.Route>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RouteServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RouteServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return route.RouteOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RouteService");
    }
  }

  private static final class RouteServiceFileDescriptorSupplier
      extends RouteServiceBaseDescriptorSupplier {
    RouteServiceFileDescriptorSupplier() {}
  }

  private static final class RouteServiceMethodDescriptorSupplier
      extends RouteServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RouteServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RouteServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RouteServiceFileDescriptorSupplier())
              .addMethod(getRequestMethod())
              .addMethod(getNominateRequestMethod())
              .addMethod(getVoteRequestMethod())
              .addMethod(getBiDirectionalRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
