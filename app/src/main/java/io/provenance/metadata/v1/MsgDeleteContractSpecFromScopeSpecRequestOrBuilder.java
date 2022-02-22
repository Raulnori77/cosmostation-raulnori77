// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: provenance/metadata/v1/tx.proto

package io.provenance.metadata.v1;

public interface MsgDeleteContractSpecFromScopeSpecRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:provenance.metadata.v1.MsgDeleteContractSpecFromScopeSpecRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * MetadataAddress for the contract specification to add.
   * </pre>
   *
   * <code>bytes contract_specification_id = 1 [(.gogoproto.nullable) = false, (.gogoproto.customtype) = "MetadataAddress", (.gogoproto.moretags) = "yaml:&#92;"specification_id&#92;""];</code>
   * @return The contractSpecificationId.
   */
  com.google.protobuf.ByteString getContractSpecificationId();

  /**
   * <pre>
   * MetadataAddress for the scope specification to add contract specification to.
   * </pre>
   *
   * <code>bytes scope_specification_id = 2 [(.gogoproto.nullable) = false, (.gogoproto.customtype) = "MetadataAddress", (.gogoproto.moretags) = "yaml:&#92;"specification_id&#92;""];</code>
   * @return The scopeSpecificationId.
   */
  com.google.protobuf.ByteString getScopeSpecificationId();

  /**
   * <code>repeated string signers = 3;</code>
   * @return A list containing the signers.
   */
  java.util.List<java.lang.String>
      getSignersList();
  /**
   * <code>repeated string signers = 3;</code>
   * @return The count of signers.
   */
  int getSignersCount();
  /**
   * <code>repeated string signers = 3;</code>
   * @param index The index of the element to return.
   * @return The signers at the given index.
   */
  java.lang.String getSigners(int index);
  /**
   * <code>repeated string signers = 3;</code>
   * @param index The index of the value to return.
   * @return The bytes of the signers at the given index.
   */
  com.google.protobuf.ByteString
      getSignersBytes(int index);
}