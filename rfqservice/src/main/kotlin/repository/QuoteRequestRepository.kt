package repository

import entity.QuoteRequest
import org.apache.ignite.springdata.repository.IgniteRepository
import org.apache.ignite.springdata.repository.config.RepositoryConfig

@RepositoryConfig(cacheName = "QuoteRequest")
interface QuoteRequestRepository : IgniteRepository<QuoteRequest, Int>