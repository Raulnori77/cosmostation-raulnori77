// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: provenance/metadata/v1/scope.proto

package io.provenance.metadata.v1;

public interface RecordOutputOrBuilder extends
    // @@protoc_insertion_point(interface_extends:provenance.metadata.v1.RecordOutput)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Hash of the data output that was output/generated for this record
   * </pre>
   *
   * <code>string hash = 1;</code>
   * @return The hash.
   */
  java.lang.String getHash();
  /**
   * <pre>
   * Hash of the data output that was output/generated for this record
   * </pre>
   *
   * <code>string hash = 1;</code>
   * @return The bytes for hash.
   */
  com.google.protobuf.ByteString
      getHashBytes();

  /**
   * <pre>
   * Status of the process execution associated with this output indicating success,failure, or pending
   * </pre>
   *
   * <code>.provenance.metadata.v1.ResultStatus status = 2;</code>
   * @return The enum numeric value on the wire for status.
   */
  int getStatusValue();
  /**
   * <pre>
   * Status of the process execution associated with this output indicating success,failure, or pending
   * </pre>
   *
   * <code>.provenance.metadata.v1.ResultStatus status = 2;</code>
   * @return The status.
   */
  io.provenance.metadata.v1.ResultStatus getStatus();
}