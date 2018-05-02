# CacheRestAPI
In-memory  cache served via REST API

Requirement:

*   Build REST API to store key-value in local memory cache
  *   API must have 4 endpoint following specification below
     *   /cache/add (This must store unique key only (existing key must be ignored), This will return true if the element was successfully added )
     *   /cache/remove (This will return true if the element was successfully removed)
     *   /cache/peek (This will return most recently added element)
     *   /cache/take (This method retrieves and removes the most recently added element from the cache and waits if necessary until an element becomes available)
