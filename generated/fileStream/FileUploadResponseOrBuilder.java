// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: fileStream.proto

package fileStream;

public interface FileUploadResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:fileStream.FileUploadResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string name = 1;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>.fileStream.FileStatus status = 2;</code>
   * @return The enum numeric value on the wire for status.
   */
  int getStatusValue();
  /**
   * <code>.fileStream.FileStatus status = 2;</code>
   * @return The status.
   */
  fileStream.FileStatus getStatus();
}
