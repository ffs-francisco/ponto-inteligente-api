package com.ffsilva.pontointeligente.services.impl

import com.ffsilva.pontointeligente.documents.Lancamento
import com.ffsilva.pontointeligente.repostories.LancamentoRepository
import com.ffsilva.pontointeligente.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class LancamentoServiceImpl(
        private val lancamentoRepository: LancamentoRepository
) : LancamentoService {

    override fun buscarPorFuncionarioId(funcionarioId: String, pageRequest: PageRequest): Page<Lancamento> =
            lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest)

    override fun buscarPorId(id: String): Optional<Lancamento> = lancamentoRepository.findById(id)

    override fun persistir(lancamento: Lancamento): Lancamento = lancamentoRepository.save(lancamento)

    override fun remover(id: String) = lancamentoRepository.deleteById(id)
}