# CacheRestAPI
In-memory  cache served via REST API

**Requirement:**

* Build REST API to store key-value in local memory cache
  *   API must have 4 endpoint following specification below
     *   /cache/add (This must store unique key only (existing key must be ignored), This will return true if the element was successfully added )
     *   /cache/remove (This will return true if the element was successfully removed)
     *   /cache/peek (This will return most recently added element)
     *   /cache/take (This method retrieves and removes the most recently added element from the cache and waits if necessary until an element becomes available)


Solution:

Technology stack:

- Java 8
- Spring boot 2.0.1.RELEASE
- gradle 4.6 (build tool)
- Github
- Intellij Idea 18
- PostMan

Instructions:

- Please install latest gradle 4.6
- Open project as gradle project from Intellij Idea(for eclipse please install gradle plugin prior to import).
- Run SpringBootApplication.main() ( now application will be up on http://localhost:8080)
- Install POSTMAN from chrome app store
- import CacheRestApi.postman_collection.json collection into postman
- Now you can run API's from already made API requests in Postman.


Instructions to run test cases:

cd {project_dir}
gradle test