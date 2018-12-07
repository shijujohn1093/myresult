package com.cuckoo.myresults.dynamo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Repository
public class DynamoRepository {

	public void putItemReadingFromS3(String result_table_name, String bucket, Region region, String fileNameToRead) {
		System.out.println("Radeing file from s3 " + fileNameToRead);
		S3Client s3 = S3Client.builder().region(region).build();
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(fileNameToRead).build();
		ResponseInputStream<GetObjectResponse> object = s3.getObject(getObjectRequest);

		BufferedReader reader = new BufferedReader(new InputStreamReader(object));
		CSVParser csvParser = null;
		try {
			reader.readLine(); // To exclude
			String[] headers = { "Exam", "EnrollmentID", "Sub1", "Sub2", "Sub3", "Result" };
			csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(headers).withIgnoreHeaderCase().withTrim());
			DynamoDbClient ddb = DynamoDbClient.create();
			for (CSVRecord csvRecord : csvParser.getRecords()) {
				System.out.println("Record Number - " + csvRecord.getRecordNumber());
				HashMap<String, AttributeValue> item_values = new HashMap<String, AttributeValue>();
				for (String header : headers) {
					item_values.put(header, AttributeValue.builder().s(csvRecord.get(header)).build());
				}
				PutItemRequest request = PutItemRequest.builder().tableName(result_table_name).item(item_values)
						.build();
				ddb.putItem(request);
			}
			csvParser.close();

		} catch (DynamoDbException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("File uploaded to dynmao db " + fileNameToRead);
	}

	public void getItem() {

		String enrollmentID = "EI00004";
		String exam = "IITJEE";
		String table_name = "result_table";

		HashMap<String, AttributeValue> key_to_get = new HashMap<String, AttributeValue>();

		key_to_get.put("EnrollmentID", AttributeValue.builder().s(enrollmentID).build());
		key_to_get.put("Exam", AttributeValue.builder().s(exam).build());


		GetItemRequest request = GetItemRequest.builder().key(key_to_get).tableName(table_name).build();
		
		
        DynamoDbClient ddb = DynamoDbClient.create();
        
        try {
            Map<String,AttributeValue> returned_item =
               ddb.getItem(request).item();
            if (returned_item != null) {
                Set<String> keys = returned_item.keySet();
                for (String key : keys) {
                    System.out.format("%s: %s\n",
                            key, returned_item.get(key).s());
                }
            } else {
                System.out.format("No item found with the key %s!\n", enrollmentID);
            }
        } catch (DynamoDbException e) {
        	e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }

	}
}
