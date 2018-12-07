package com.cuckoo.myresults.dynamo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cuckoo.myresults.model.Result;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

@Repository
public class ResultRepositoryImpl implements ResultRepository {
	DynamoDbClient ddb = DynamoDbClient.create();

	@Override
	public Result getResult(String exam, String enrollmentId) {

		
		String table_name = "result_table";

		HashMap<String, AttributeValue> key_to_get = new HashMap<String, AttributeValue>();

		key_to_get.put("EnrollmentID", AttributeValue.builder().s(enrollmentId).build());
		key_to_get.put("Exam", AttributeValue.builder().s(exam).build());

		GetItemRequest request = GetItemRequest.builder().key(key_to_get).tableName(table_name).build();

		Result result = new Result();
		result.setEnrollmentId(enrollmentId);
		result.setExam(exam);

		try {

			Map<String, AttributeValue> returned_item = ddb.getItem(request).item();
			if (returned_item != null) {
				Set<String> keys = returned_item.keySet();

				for (String key : keys) {
					System.out.format("=========================%s: %s\n", key, returned_item.get(key).s());
					populateResult(result, key, returned_item.get(key).s());
				}
				System.out.println("Result fetched"+request.toString());
				return result;
			} else {
				System.out.format("No item found with the key %s!\n", enrollmentId);
			}
		} catch (DynamoDbException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return null;

	}



	private Result populateResult(Result result,String key, String value) {
		if (key.equals("ENROLLMENTID")) { 
			result.setEnrollmentId(value);
		}else if (key.equals("EXAM")) {
			result.setExam(value);
		}else if (key.toUpperCase().equals("RESULT")) {
			result.setResult(value);
		}else if (key.toUpperCase().equals("SUB1")) {
			result.setSubject1(value);
		}else if (key.toUpperCase().equals("SUB2")) {
			result.setSubject2(value);
		}else if (key.toUpperCase().equals("SUB3")) {
			result.setSubject3(value);
		}
		return result;
	}
}
