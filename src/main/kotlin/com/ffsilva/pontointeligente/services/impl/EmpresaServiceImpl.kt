package com.ffsilva.pontointeligente.services.impl

import com.ffsilva.pontointeligente.documents.Empresa
import com.ffsilva.pontointeligente.repostories.EmpresaRepository
import com.ffsilva.pontointeligente.services.EmpresaService
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmpresaServiceImpl(
        private val empresaRepository: EmpresaRepository
) : EmpresaService {

    override fun buscarPorCnpj(cnpj: String): Optional<Empresa> = empresaRepository.findByCnpj(cnpj)

    override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)
}