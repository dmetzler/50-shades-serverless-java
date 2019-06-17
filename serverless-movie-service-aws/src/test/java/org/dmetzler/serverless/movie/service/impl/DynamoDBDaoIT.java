package org.dmetzler.serverless.movie.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dmetzler.serverless.service.AbstractMovieDaoTest;
import org.dmetzler.serverless.service.MovieDao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import cloud.localstack.DockerTestUtils;

public class DynamoDBDaoIT extends AbstractMovieDaoTest {

    @Override
    protected MovieDao getMovieDao() {
        AmazonDynamoDB dynamoClient = DockerTestUtils.getClientDynamoDb();
        createTable(dynamoClient, DynamoDBMovieDao.getMoviesTableName());

        return new DynamoDBMovieDao(dynamoClient);
    }

    public static void createTable(AmazonDynamoDB dynamoClient, String tableName) {
        try {
            DynamoDB dynamoDB = new DynamoDB(dynamoClient);
            Table table = dynamoDB.getTable(tableName);
            if (table != null) {
                try {
                    table.delete();

                    table.waitForDelete();

                } catch (ResourceNotFoundException e) {

                }
            }

            List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("S"));

            List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

            CreateTableRequest request = new CreateTableRequest().withTableName(tableName)
                                                                 .withKeySchema(keySchema)
                                                                 .withAttributeDefinitions(attributeDefinitions)
                                                                 .withProvisionedThroughput(
                                                                         new ProvisionedThroughput().withReadCapacityUnits(
                                                                                 5L).withWriteCapacityUnits(6L));

            table = dynamoDB.createTable(request);

            table.waitForActive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
