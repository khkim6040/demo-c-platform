package io.github.khkim6040.sample.domain

import java.util.concurrent.ConcurrentHashMap

interface ContractRepository {
    fun save(contract: Contract): Contract
    fun findById(id: String): Contract
    fun findAll(): List<Contract>
}

class InMemoryContractRepository : ContractRepository {
    private val store = ConcurrentHashMap<String, Contract>()

    override fun save(contract: Contract): Contract {
        store[contract.id] = contract
        return contract
    }

    override fun findById(id: String): Contract =
        store[id] ?: throw NoSuchElementException("contract $id not found")

    override fun findAll(): List<Contract> = store.values.sortedBy { it.id }
}
