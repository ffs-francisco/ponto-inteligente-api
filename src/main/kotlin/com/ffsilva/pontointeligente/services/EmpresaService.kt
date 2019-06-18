package com.ffsilva.pontointeligente.services

import com.ffsilva.pontointeligente.documents.Empresa
import java.util.*

interface EmpresaService {

    fun buscarPorCnpj(cnpj: String): Optional<Empresa>

    fun persistir(empresa: Empresa): Empresa
}