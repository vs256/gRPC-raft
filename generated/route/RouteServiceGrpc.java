package route;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.44.1)",
    comments = "Source: route.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RouteServiceGrpc {

  private RouteServiceGrpc() {}

  public static final String SERVICE_NAME = "route.RouteService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<route.Route,
      route.Route> getBlockingServerRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "blockingServerRequest",
      requestType = route.Route.class,
      responseType = route.Route.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<route.Route,
      route.Route> getBlockingServerRequestMethod() {
    io.grpc.MethodDescriptor<route.Route, route.Route> getBlockingServerRequestMethod;
    if ((getBlockingServerRequestMethod = RouteServiceGrpc.getBlockingServerRequestMethod) == null) {
      synchronized (RouteServiceGrpc.class) {
        if ((getBlockingServerRequestMethod = RouteServiceGrpc.getBlockingServerRequestMethod) == null) {
          RouteServiceGrpc.getBlockingServerRequestMethod = getBlockingServerRequestMethod =
              io.grpc.MethodDescriptor.<route.Route, route.Route>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "blockingServerRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  route.Route.getDefaultInstance()))
              .setSchemaDescriptor(new RouteServiceMethodDescriptorSupplier("blockingServerRequest"))
              .build();
        }
      }
    }
    return getBlockingServerRequestMethod;
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
    public void blockingServerRequest(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBlockingServerRequestMethod(), responseObserver);
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
            getBlockingServerRequestMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                route.Route,
                route.Route>(
                  this, METHODID_BLOCKING_SERVER_REQUEST)))
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
    public void blockingServerRequest(route.Route request,
        io.grpc.stub.StreamObserver<route.Route> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBlockingServerRequestMethod(), getCallOptions()), request, responseObserver);
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
    public route.Route blockingServerRequest(route.Route request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBlockingServerRequestMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<route.Route> blockingServerRequest(
        route.Route request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBlockingServerRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BLOCKING_SERVER_REQUEST = 0;
  private static final int METHODID_BI_DIRECTIONAL_REQUEST = 1;

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
        case METHODID_BLOCKING_SERVER_REQUEST:
          serviceImpl.blockingServerRequest((route.Route) request,
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
              .addMethod(getBlockingServerRequestMethod())
              .addMethod(getBiDirectionalRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
