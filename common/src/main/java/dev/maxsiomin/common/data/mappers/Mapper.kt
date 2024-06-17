package dev.maxsiomin.common.data.mappers

// Sealed so that no classes from other modules would be able to inherit from this interface
sealed interface BaseMapper<Data, Domain>

interface ToDataMapper<Data, Domain> : BaseMapper<Data, Domain> {

    fun toData(domain: Domain): Data

}

interface ToDomainMapper<Data, Domain> : BaseMapper<Data, Domain> {

    fun toDomain(data: Data): Domain

}

interface BidirectionalMapper<Data, Domain> : BaseMapper<Data, Domain> {

    fun toDomain(data: Data): Domain

    fun toData(domain: Domain): Data

}
