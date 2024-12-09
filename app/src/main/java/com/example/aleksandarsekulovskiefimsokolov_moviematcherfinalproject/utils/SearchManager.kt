package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.FacetFilters
import com.algolia.client.model.search.SearchParamsObject

class SearchManager {
    private val appID = "1QHYV4G402"
    private val apiKey = "ca9c08efd5997d552f077269757dd817"
    private val client = SearchClient(appID, apiKey)

    suspend fun searchUsers(query: String, indexName: String) {
            try {
                var response = client.searchSingleIndex(
                    indexName = indexName,
                    searchParams = SearchParamsObject(
                        query = query,
                    ),
                )
                println("Search results: ${response}")
                println("Search results: ${response.hits}")
                println("Search results: ${response.hits.toList()}")

                response.hits.toList()
            } catch (e: Exception) {
                println("Search error: ${e.message}")
                emptyList()
            }
        }
}
