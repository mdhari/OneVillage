package edu.sjsu.carbonated.mongodbaccessors;

/**
 *      Copyright (C) 2008 10gen Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/**
 * @author Michael Hari
 * @date: Nov 6, 2011
 */

import java.net.UnknownHostException;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

import edu.sjsu.carbonated.data.AlbumResource;
import edu.sjsu.carbonated.data.PhotoResource;
import edu.sjsu.carbonated.util.Utils;

public class MongoDBAlbum {

	protected static final String albumCollection = "Album";
	protected static final String photoCollection = "Photo";
	protected static final String sDB = "OneVillage";
	protected static final String sHost = "localhost";
	protected static final String photo_url_location = "http://localhost:8080/albums/";
	
	

	Mongo m = null;
	DB db = null;
	DBCollection albumColl = null;
	DBCollection photoColl = null;
	

	public MongoDBAlbum() {

		try {
			m = new Mongo(sHost);
			DB db = m.getDB(sDB);
			albumColl = db.getCollection(albumCollection);
			photoColl = db.getCollection(photoCollection);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void printCollections() {

		Set<String> colls = db.getCollectionNames();
		for (String s : colls) {
			System.out.println(s);
		}
	}

	public void truncateDBCollection() {
		albumColl.drop();
	}

	/**
	 * Uses the getMap method in AlbumResource to insert into the database.
	 * <p>
	 * Make sure no fields are null and that album_id has already been set
	 * before reaching here.
	 * 
	 * @param albumRes
	 */
	public void addAlbum(AlbumResource albumRes) {

		BasicDBObject info = new BasicDBObject();
		info.putAll(albumRes.getMap());
		albumColl.insert(info);

	}
	
	public void addPhotoToAlbum(AlbumResource albumRes){
		 
		BasicDBObject info = new BasicDBObject();
		info.putAll(albumRes.getMap());
		info.put("photo_url", photo_url_location+albumRes.getAlbum_id()+"/"+albumRes.getPhoto_id() +".png");
		photoColl.insert(info);
		
	}
	
	public String getPhoto(String album_id, String photo_id) {

		String toReturn = "";
		
		BasicDBObject query = new BasicDBObject("$and", JSON.parse("[{\"album_id\":\""+album_id+"\"},{\"photo_id\":\""+photo_id+"\"}]"));
		
		DBCursor cur = photoColl.find(query, new BasicDBObject("_id",0));

		while (cur.hasNext()) {
			toReturn += cur.next();
		}

		
		return toReturn;
		
		
	}
	
	public String getAllPhotoInAlbum(String album_id) {

		String toReturn = "[";
		
		DBCursor cur = photoColl.find(new BasicDBObject(), new BasicDBObject("_id",0));

		while (cur.hasNext()) {
			toReturn += cur.next();
		}
		
		toReturn += "]";
		
		return toReturn;
		
		
	}

	public String getAllAlbums() {

		String toReturn = "[";
		
		DBCursor cur = albumColl.find(new BasicDBObject(), new BasicDBObject("_id",0));

		while (cur.hasNext()) {
			toReturn += cur.next();
		}
		
		toReturn += "]";
		
		return toReturn;
		
		
	}
	
	public String getAlbumsByUser(String user_id){
		
		String toReturn = "[";
		
		DBCursor cur = albumColl.find(new BasicDBObject("user_id",user_id),new BasicDBObject("_id",0));

		while (cur.hasNext()) {
			toReturn += cur.next();
		}
		
		toReturn += "]";
		
		return toReturn;
	}

	/**
	 * Will update the name and description of an album. All Resources must have
	 * a getMap method returning all those that need to be updated only.
	 * <p>
	 * No null values should be present in the map.
	 * 
	 * @param album_id
	 * @param albumRes
	 */
	public void updateAlbum(String album_id, AlbumResource albumRes) {

		BasicDBObject query = new BasicDBObject("$and", JSON.parse("[{\"album_id\":\""+album_id+"\"},{\"user_id\":\""+albumRes.getUser_id()+"\"}]"));

		BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(
				albumRes.getUpdateAlbumMap()));

		WriteResult wr = albumColl.update(query, update);
		
		// mongodb way of saying there was no record updated...
		if(wr.getLastError().getString("updatedExisting") == "false"){
			throw new WebApplicationException(Response.status(404)
					.entity("Album not found").build());
		}

	}
	
	public void updatePhoto(String album_id, String photo_id, AlbumResource albumRes) {

		//BasicDBObject query = new BasicDBObject("$and", new BasicDBObject("album_id",album_id).append("photo_id", photo_id));
		BasicDBObject query = new BasicDBObject("$and", JSON.parse("[{\"album_id\":\""+album_id+"\"},{\"photo_id\":\""+photo_id+"\"}]"));
		
		BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(
				albumRes.getMap()));
		
		WriteResult wr = photoColl.update(query, update);
		
		if(wr.getLastError().getString("updatedExisting") == "false"){
			throw new WebApplicationException(Response.status(404)
					.entity("Album/Photo not found").build());
		}

	}

	/**
	 * Currently only requires album_id to be passed since Authentication system
	 * is not up. Removes the metadata from the data store.
	 * 
	 * @param album_id
	 * @param user_id
	 */
	public void removeAlbum(String album_id, String user_id) {

		BasicDBObject removeQuery = new BasicDBObject("$and", JSON.parse("[{\"album_id\":\""+album_id+"\"},{\"user_id\":\""+user_id+"\"}]"));

		albumColl.remove(removeQuery);
		photoColl.remove(new BasicDBObject("album_id",album_id));
//		System.out.println(wr.getLastError().getString("n"));
//		// mongodb way of detecting deletion
//		if(wr.getLastError().getString("n") == "0"){
//			throw new WebApplicationException(Response.status(404)
//					.entity("Album not found").build());
//		}

	}
	
	public void removePhoto(String user_id,String photo_id) {

		BasicDBObject removeQuery = new BasicDBObject("$and", JSON.parse("[{\"photo_id\":\""+photo_id+"\"},{\"user_id\":\""+user_id+"\"}]"));

		photoColl.remove(removeQuery);

	}

	// public static void main(String[] args) throws Exception {
	//
	// // connect to the local database server
	// Mongo m = new Mongo();
	//
	// // get handle to "mydb"
	// DB db = m.getDB( "mydb" );
	//
	// // Authenticate - optional
	// // boolean auth = db.authenticate("foo", "bar");
	//
	//
	// // get a list of the collections in this database and print them out
	// Set<String> colls = db.getCollectionNames();
	// for (String s : colls) {
	// System.out.println(s);
	// }
	//
	// // get a collection object to work with
	// DBCollection coll = db.getCollection("testCollection");
	//
	// // drop all the data in it
	// coll.drop();
	//
	//
	// // make a document and insert it
	// BasicDBObject doc = new BasicDBObject();
	//
	//
	// doc.put("name", "MongoDB");
	// doc.put("type", "database");
	// doc.put("count", 1);
	//
	// BasicDBObject info = new BasicDBObject();
	//
	// info.put("x", 203);
	// info.put("y", 102);
	//
	// doc.put("info", info);
	//
	// coll.insert(doc);
	//
	// // get it (since it's the only one in there since we dropped the rest
	// earlier on)
	// DBObject myDoc = coll.findOne();
	// System.out.println(myDoc);
	//
	// // now, lets add lots of little documents to the collection so we can
	// explore queries and cursors
	// for (int i=0; i < 100; i++) {
	// coll.insert(new BasicDBObject().append("i", i));
	// }
	// System.out.println("total # of documents after inserting 100 small ones (should be 101) "
	// + coll.getCount());
	//
	// // lets get all the documents in the collection and print them out
	// DBCursor cur = coll.find();
	// while(cur.hasNext()) {
	// System.out.println(cur.next());
	// }
	//
	// // now use a query to get 1 document out
	// BasicDBObject query = new BasicDBObject();
	// query.put("i", 71);
	// cur = coll.find(query);
	//
	// while(cur.hasNext()) {
	// System.out.println(cur.next());
	// }
	//
	// // now use a range query to get a larger subset
	// query = new BasicDBObject();
	// query.put("i", new BasicDBObject("$gt", 50)); // i.e. find all where i >
	// 50
	// cur = coll.find(query);
	//
	// while(cur.hasNext()) {
	// System.out.println(cur.next());
	// }
	//
	// // range query with multiple contstraings
	// query = new BasicDBObject();
	// query.put("i", new BasicDBObject("$gt", 20).append("$lte", 30)); // i.e.
	// 20 < i <= 30
	// cur = coll.find(query);
	//
	// while(cur.hasNext()) {
	// System.out.println(cur.next());
	// }
	//
	// // create an index on the "i" field
	// coll.createIndex(new BasicDBObject("i", 1)); // create index on "i",
	// ascending
	//
	//
	// // list the indexes on the collection
	// List<DBObject> list = coll.getIndexInfo();
	// for (DBObject o : list) {
	// System.out.println(o);
	// }
	//
	// // See if the last operation had an error
	// System.out.println("Last error : " + db.getLastError());
	//
	// // see if any previous operation had an error
	// System.out.println("Previous error : " + db.getPreviousError());
	//
	// // force an error
	// db.forceError();
	//
	// // See if the last operation had an error
	// System.out.println("Last error : " + db.getLastError());
	//
	// db.resetError();
	// }
}