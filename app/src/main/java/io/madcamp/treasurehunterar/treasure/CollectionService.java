package io.madcamp.treasurehunterar.treasure;

import java.util.List;

import io.madcamp.treasurehunterar.collection.Collection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CollectionService {
    @GET("/collection/{collectionNum}")
    Call<Collection> getCollectionById(@Path("collectionNum") String collectionNum);

    @GET("/collections")
    Call<List<Collection>> getCollections();

    @POST("/collection/found/{collectionNum}")
    Call<Object> collectionFound(@Path("collectionNum") String collectionNum);
}
