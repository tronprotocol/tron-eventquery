package org.tron.trongeventquery;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface EventLogRepository extends MongoRepository<EventLogEntity, String> {

  @Override
  List<EventLogEntity> findAll();

  @Query("{ 'transaction_id' : ?0, 'resource_Node' : {$exists : true} }")
  List<EventLogEntity> findByTransactionId(String transactionId);

  @Query("{'contract_address' : ?0, 'resource_Node' : {$exists : true}}")
  List<EventLogEntity> findByContractAddress(String contractAddress);

  @Query("{'contract_address' : ?0, 'event_name' : ?1, 'resource_Node' : {$exists : true} }")
  List<EventLogEntity> findByContractAddressAndEntryName(String contractAddress, String entryName);

  @Query("{'contract_address' : ?0, 'event_name': ?1, 'block_number' : ?2, 'resource_Node' : {$exists : true} }")
  List<EventLogEntity> findByContractAddressAndEntryNameAndBlockNumber(String contractAddress,
      String entryName, Long blockNumber);

  @Query("{'contract_address' : ?0, 'event_name': ?1, 'block_number' : ?2, 'resource_Node' : {$exists : true} }")
  List<EventLogEntity> findTotal(String contractAddress,
      String entryName, Long blockNumber);

  /***************** with pagniate ***********************/
  @Query(QueryFactory.findByContractAndEventSinceTimestamp)
  List<EventLogEntity> findByContractAndEventSinceTimestamp(String contractAddress, String eventName, Long timestamp, Pageable pageable);

  @Query("{ '$or' : [ {'timeStamp' : ?0}, {'timeStamp' : {$gt : ?0}} ], 'resource_Node' : {$exists : true} }")
    // return all event triggered after a certain timestamp
  List<EventLogEntity> findByBlockTimestampGreaterThan(Long timestamp,  Pageable pageable);

  @Query("{ '$or' : [ {'block_timestamp' : ?0}, {'block_timestamp' : {$gt : ?0}} ], 'contract_address' : ?1, 'resource_Node' : {$exists : true}}")
  List<EventLogEntity> findByBlockTimestampAndContractAddressGreaterThan(Long timestamp, String contract_address, Pageable pageable);

}