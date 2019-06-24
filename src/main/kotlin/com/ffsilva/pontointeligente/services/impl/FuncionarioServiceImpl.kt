package com.ffsilva.pontointeligente.services.impl

import com.ffsilva.pontointeligente.documents.Funcionario
import com.ffsilva.pontointeligente.repostories.FuncionarioRepository
import com.ffsilva.pontointeligente.services.FuncionarioService
import org.springframework.stereotype.Service
import java.util.*

@Service
class FuncionarioServiceImpl(
        val funcionarioRepository: FuncionarioRepository
) : FuncionarioService {

    override fun persistir(funcionario: Funcionario): Funcionario = funcionarioRepository.save(funcionario)

    override fun buscarPorCpf(cpf: String): Funcionario? = funcionarioRepository.findByCpf(cpf)

    override fun buscarPorEmail(email: String): Funcionario? = funcionarioRepository.findByEmail(email)

    override fun buscarPorId(id: String): Optional<Funcionario> = funcionarioRepository.findById(id)
}