// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: fileStream.proto

package fileStream;

/**
 * Protobuf type {@code fileStream.FileUploadRequest}
 */
public final class FileUploadRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:fileStream.FileUploadRequest)
    FileUploadRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use FileUploadRequest.newBuilder() to construct.
  private FileUploadRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private FileUploadRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new FileUploadRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private FileUploadRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            fileStream.MetaData.Builder subBuilder = null;
            if (requestCase_ == 1) {
              subBuilder = ((fileStream.MetaData) request_).toBuilder();
            }
            request_ =
                input.readMessage(fileStream.MetaData.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((fileStream.MetaData) request_);
              request_ = subBuilder.buildPartial();
            }
            requestCase_ = 1;
            break;
          }
          case 18: {
            fileStream.FileContent.Builder subBuilder = null;
            if (requestCase_ == 2) {
              subBuilder = ((fileStream.FileContent) request_).toBuilder();
            }
            request_ =
                input.readMessage(fileStream.FileContent.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((fileStream.FileContent) request_);
              request_ = subBuilder.buildPartial();
            }
            requestCase_ = 2;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (com.google.protobuf.UninitializedMessageException e) {
      throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return fileStream.FileStream.internal_static_fileStream_FileUploadRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return fileStream.FileStream.internal_static_fileStream_FileUploadRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            fileStream.FileUploadRequest.class, fileStream.FileUploadRequest.Builder.class);
  }

  private int requestCase_ = 0;
  private java.lang.Object request_;
  public enum RequestCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    METADATA(1),
    FILE(2),
    REQUEST_NOT_SET(0);
    private final int value;
    private RequestCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static RequestCase valueOf(int value) {
      return forNumber(value);
    }

    public static RequestCase forNumber(int value) {
      switch (value) {
        case 1: return METADATA;
        case 2: return FILE;
        case 0: return REQUEST_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public RequestCase
  getRequestCase() {
    return RequestCase.forNumber(
        requestCase_);
  }

  public static final int METADATA_FIELD_NUMBER = 1;
  /**
   * <code>.fileStream.MetaData metadata = 1;</code>
   * @return Whether the metadata field is set.
   */
  @java.lang.Override
  public boolean hasMetadata() {
    return requestCase_ == 1;
  }
  /**
   * <code>.fileStream.MetaData metadata = 1;</code>
   * @return The metadata.
   */
  @java.lang.Override
  public fileStream.MetaData getMetadata() {
    if (requestCase_ == 1) {
       return (fileStream.MetaData) request_;
    }
    return fileStream.MetaData.getDefaultInstance();
  }
  /**
   * <code>.fileStream.MetaData metadata = 1;</code>
   */
  @java.lang.Override
  public fileStream.MetaDataOrBuilder getMetadataOrBuilder() {
    if (requestCase_ == 1) {
       return (fileStream.MetaData) request_;
    }
    return fileStream.MetaData.getDefaultInstance();
  }

  public static final int FILE_FIELD_NUMBER = 2;
  /**
   * <code>.fileStream.FileContent file = 2;</code>
   * @return Whether the file field is set.
   */
  @java.lang.Override
  public boolean hasFile() {
    return requestCase_ == 2;
  }
  /**
   * <code>.fileStream.FileContent file = 2;</code>
   * @return The file.
   */
  @java.lang.Override
  public fileStream.FileContent getFile() {
    if (requestCase_ == 2) {
       return (fileStream.FileContent) request_;
    }
    return fileStream.FileContent.getDefaultInstance();
  }
  /**
   * <code>.fileStream.FileContent file = 2;</code>
   */
  @java.lang.Override
  public fileStream.FileContentOrBuilder getFileOrBuilder() {
    if (requestCase_ == 2) {
       return (fileStream.FileContent) request_;
    }
    return fileStream.FileContent.getDefaultInstance();
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (requestCase_ == 1) {
      output.writeMessage(1, (fileStream.MetaData) request_);
    }
    if (requestCase_ == 2) {
      output.writeMessage(2, (fileStream.FileContent) request_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (requestCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (fileStream.MetaData) request_);
    }
    if (requestCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (fileStream.FileContent) request_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof fileStream.FileUploadRequest)) {
      return super.equals(obj);
    }
    fileStream.FileUploadRequest other = (fileStream.FileUploadRequest) obj;

    if (!getRequestCase().equals(other.getRequestCase())) return false;
    switch (requestCase_) {
      case 1:
        if (!getMetadata()
            .equals(other.getMetadata())) return false;
        break;
      case 2:
        if (!getFile()
            .equals(other.getFile())) return false;
        break;
      case 0:
      default:
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    switch (requestCase_) {
      case 1:
        hash = (37 * hash) + METADATA_FIELD_NUMBER;
        hash = (53 * hash) + getMetadata().hashCode();
        break;
      case 2:
        hash = (37 * hash) + FILE_FIELD_NUMBER;
        hash = (53 * hash) + getFile().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static fileStream.FileUploadRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static fileStream.FileUploadRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static fileStream.FileUploadRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static fileStream.FileUploadRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static fileStream.FileUploadRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static fileStream.FileUploadRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static fileStream.FileUploadRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static fileStream.FileUploadRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static fileStream.FileUploadRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static fileStream.FileUploadRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static fileStream.FileUploadRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static fileStream.FileUploadRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(fileStream.FileUploadRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code fileStream.FileUploadRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:fileStream.FileUploadRequest)
      fileStream.FileUploadRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return fileStream.FileStream.internal_static_fileStream_FileUploadRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return fileStream.FileStream.internal_static_fileStream_FileUploadRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              fileStream.FileUploadRequest.class, fileStream.FileUploadRequest.Builder.class);
    }

    // Construct using fileStream.FileUploadRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      requestCase_ = 0;
      request_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return fileStream.FileStream.internal_static_fileStream_FileUploadRequest_descriptor;
    }

    @java.lang.Override
    public fileStream.FileUploadRequest getDefaultInstanceForType() {
      return fileStream.FileUploadRequest.getDefaultInstance();
    }

    @java.lang.Override
    public fileStream.FileUploadRequest build() {
      fileStream.FileUploadRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public fileStream.FileUploadRequest buildPartial() {
      fileStream.FileUploadRequest result = new fileStream.FileUploadRequest(this);
      if (requestCase_ == 1) {
        if (metadataBuilder_ == null) {
          result.request_ = request_;
        } else {
          result.request_ = metadataBuilder_.build();
        }
      }
      if (requestCase_ == 2) {
        if (fileBuilder_ == null) {
          result.request_ = request_;
        } else {
          result.request_ = fileBuilder_.build();
        }
      }
      result.requestCase_ = requestCase_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof fileStream.FileUploadRequest) {
        return mergeFrom((fileStream.FileUploadRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(fileStream.FileUploadRequest other) {
      if (other == fileStream.FileUploadRequest.getDefaultInstance()) return this;
      switch (other.getRequestCase()) {
        case METADATA: {
          mergeMetadata(other.getMetadata());
          break;
        }
        case FILE: {
          mergeFile(other.getFile());
          break;
        }
        case REQUEST_NOT_SET: {
          break;
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      fileStream.FileUploadRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (fileStream.FileUploadRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int requestCase_ = 0;
    private java.lang.Object request_;
    public RequestCase
        getRequestCase() {
      return RequestCase.forNumber(
          requestCase_);
    }

    public Builder clearRequest() {
      requestCase_ = 0;
      request_ = null;
      onChanged();
      return this;
    }


    private com.google.protobuf.SingleFieldBuilderV3<
        fileStream.MetaData, fileStream.MetaData.Builder, fileStream.MetaDataOrBuilder> metadataBuilder_;
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     * @return Whether the metadata field is set.
     */
    @java.lang.Override
    public boolean hasMetadata() {
      return requestCase_ == 1;
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     * @return The metadata.
     */
    @java.lang.Override
    public fileStream.MetaData getMetadata() {
      if (metadataBuilder_ == null) {
        if (requestCase_ == 1) {
          return (fileStream.MetaData) request_;
        }
        return fileStream.MetaData.getDefaultInstance();
      } else {
        if (requestCase_ == 1) {
          return metadataBuilder_.getMessage();
        }
        return fileStream.MetaData.getDefaultInstance();
      }
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     */
    public Builder setMetadata(fileStream.MetaData value) {
      if (metadataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        request_ = value;
        onChanged();
      } else {
        metadataBuilder_.setMessage(value);
      }
      requestCase_ = 1;
      return this;
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     */
    public Builder setMetadata(
        fileStream.MetaData.Builder builderForValue) {
      if (metadataBuilder_ == null) {
        request_ = builderForValue.build();
        onChanged();
      } else {
        metadataBuilder_.setMessage(builderForValue.build());
      }
      requestCase_ = 1;
      return this;
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     */
    public Builder mergeMetadata(fileStream.MetaData value) {
      if (metadataBuilder_ == null) {
        if (requestCase_ == 1 &&
            request_ != fileStream.MetaData.getDefaultInstance()) {
          request_ = fileStream.MetaData.newBuilder((fileStream.MetaData) request_)
              .mergeFrom(value).buildPartial();
        } else {
          request_ = value;
        }
        onChanged();
      } else {
        if (requestCase_ == 1) {
          metadataBuilder_.mergeFrom(value);
        } else {
          metadataBuilder_.setMessage(value);
        }
      }
      requestCase_ = 1;
      return this;
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     */
    public Builder clearMetadata() {
      if (metadataBuilder_ == null) {
        if (requestCase_ == 1) {
          requestCase_ = 0;
          request_ = null;
          onChanged();
        }
      } else {
        if (requestCase_ == 1) {
          requestCase_ = 0;
          request_ = null;
        }
        metadataBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     */
    public fileStream.MetaData.Builder getMetadataBuilder() {
      return getMetadataFieldBuilder().getBuilder();
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     */
    @java.lang.Override
    public fileStream.MetaDataOrBuilder getMetadataOrBuilder() {
      if ((requestCase_ == 1) && (metadataBuilder_ != null)) {
        return metadataBuilder_.getMessageOrBuilder();
      } else {
        if (requestCase_ == 1) {
          return (fileStream.MetaData) request_;
        }
        return fileStream.MetaData.getDefaultInstance();
      }
    }
    /**
     * <code>.fileStream.MetaData metadata = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        fileStream.MetaData, fileStream.MetaData.Builder, fileStream.MetaDataOrBuilder> 
        getMetadataFieldBuilder() {
      if (metadataBuilder_ == null) {
        if (!(requestCase_ == 1)) {
          request_ = fileStream.MetaData.getDefaultInstance();
        }
        metadataBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            fileStream.MetaData, fileStream.MetaData.Builder, fileStream.MetaDataOrBuilder>(
                (fileStream.MetaData) request_,
                getParentForChildren(),
                isClean());
        request_ = null;
      }
      requestCase_ = 1;
      onChanged();;
      return metadataBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        fileStream.FileContent, fileStream.FileContent.Builder, fileStream.FileContentOrBuilder> fileBuilder_;
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     * @return Whether the file field is set.
     */
    @java.lang.Override
    public boolean hasFile() {
      return requestCase_ == 2;
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     * @return The file.
     */
    @java.lang.Override
    public fileStream.FileContent getFile() {
      if (fileBuilder_ == null) {
        if (requestCase_ == 2) {
          return (fileStream.FileContent) request_;
        }
        return fileStream.FileContent.getDefaultInstance();
      } else {
        if (requestCase_ == 2) {
          return fileBuilder_.getMessage();
        }
        return fileStream.FileContent.getDefaultInstance();
      }
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     */
    public Builder setFile(fileStream.FileContent value) {
      if (fileBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        request_ = value;
        onChanged();
      } else {
        fileBuilder_.setMessage(value);
      }
      requestCase_ = 2;
      return this;
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     */
    public Builder setFile(
        fileStream.FileContent.Builder builderForValue) {
      if (fileBuilder_ == null) {
        request_ = builderForValue.build();
        onChanged();
      } else {
        fileBuilder_.setMessage(builderForValue.build());
      }
      requestCase_ = 2;
      return this;
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     */
    public Builder mergeFile(fileStream.FileContent value) {
      if (fileBuilder_ == null) {
        if (requestCase_ == 2 &&
            request_ != fileStream.FileContent.getDefaultInstance()) {
          request_ = fileStream.FileContent.newBuilder((fileStream.FileContent) request_)
              .mergeFrom(value).buildPartial();
        } else {
          request_ = value;
        }
        onChanged();
      } else {
        if (requestCase_ == 2) {
          fileBuilder_.mergeFrom(value);
        } else {
          fileBuilder_.setMessage(value);
        }
      }
      requestCase_ = 2;
      return this;
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     */
    public Builder clearFile() {
      if (fileBuilder_ == null) {
        if (requestCase_ == 2) {
          requestCase_ = 0;
          request_ = null;
          onChanged();
        }
      } else {
        if (requestCase_ == 2) {
          requestCase_ = 0;
          request_ = null;
        }
        fileBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     */
    public fileStream.FileContent.Builder getFileBuilder() {
      return getFileFieldBuilder().getBuilder();
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     */
    @java.lang.Override
    public fileStream.FileContentOrBuilder getFileOrBuilder() {
      if ((requestCase_ == 2) && (fileBuilder_ != null)) {
        return fileBuilder_.getMessageOrBuilder();
      } else {
        if (requestCase_ == 2) {
          return (fileStream.FileContent) request_;
        }
        return fileStream.FileContent.getDefaultInstance();
      }
    }
    /**
     * <code>.fileStream.FileContent file = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        fileStream.FileContent, fileStream.FileContent.Builder, fileStream.FileContentOrBuilder> 
        getFileFieldBuilder() {
      if (fileBuilder_ == null) {
        if (!(requestCase_ == 2)) {
          request_ = fileStream.FileContent.getDefaultInstance();
        }
        fileBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            fileStream.FileContent, fileStream.FileContent.Builder, fileStream.FileContentOrBuilder>(
                (fileStream.FileContent) request_,
                getParentForChildren(),
                isClean());
        request_ = null;
      }
      requestCase_ = 2;
      onChanged();;
      return fileBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:fileStream.FileUploadRequest)
  }

  // @@protoc_insertion_point(class_scope:fileStream.FileUploadRequest)
  private static final fileStream.FileUploadRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new fileStream.FileUploadRequest();
  }

  public static fileStream.FileUploadRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FileUploadRequest>
      PARSER = new com.google.protobuf.AbstractParser<FileUploadRequest>() {
    @java.lang.Override
    public FileUploadRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new FileUploadRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<FileUploadRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<FileUploadRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public fileStream.FileUploadRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

