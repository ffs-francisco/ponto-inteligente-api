package com.ffsilva.pontointeligente.security

import com.ffsilva.pontointeligente.documents.Funcionario
import com.ffsilva.pontointeligente.services.FuncionarioService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class FuncionarioDetailsService(val funcionarioService: FuncionarioService) : UserDetailsService {

    override fun loadUserByUsername(userName: String?): UserDetails {
        if (userName != null) {
            val funcionario: Funcionario? = funcionarioService.buscarPorEmail(userName)
            if (funcionario != null)
                return FuncionarioPrincipal(funcionario)
        }

        throw  UsernameNotFoundException(userName)
    }
}