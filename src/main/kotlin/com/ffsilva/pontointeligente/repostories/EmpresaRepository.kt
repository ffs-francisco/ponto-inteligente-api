package com.ffsilva.pontointeligente.repostories

import com.ffsilva.pontointeligente.documents.Empresa
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EmpresaRepository : MongoRepository<Empresa, String> {

    fun findByCnpj(cnpj: String): Optional<Empresa>
}