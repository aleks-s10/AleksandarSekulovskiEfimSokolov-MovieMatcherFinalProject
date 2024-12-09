package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.*
import com.algolia.client.model.search.SearchParamsObject

class SearchManager {
    private val appID = "1QHYV4G402"
    private val apiKey = "ca9c08efd5997d552f077269757dd817"
    private val client = SearchClient(appID, apiKey)

    suspend fun searchUsers(query: String, indexName: String): List<Hit> {
        var response = emptyList<Hit>()
        try {
            response = client.searchSingleIndex(
                indexName = indexName,
                searchParams = SearchParamsObject(
                    query = query,
                ),
            ).hits
            println("Search results: ${response}")
        } catch (e: Exception) {
            println("Search error: ${e.message}")
        }
        if (response.isNotEmpty()) {
            for (hit in response) {
                val additionalProperties = hit.additionalProperties
                println("Additional Properties: $additionalProperties")
                println("Username: ${additionalProperties?.get("Username")}")
            }
        } else {
            println("No search results found.")
        }
        return response
    }
}
