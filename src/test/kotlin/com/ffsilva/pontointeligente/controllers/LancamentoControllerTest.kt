package com.ffsilva.pontointeligente.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.ffsilva.pontointeligente.documents.Funcionario
import com.ffsilva.pontointeligente.documents.Lancamento
import com.ffsilva.pontointeligente.dtos.LancamentoDto
import com.ffsilva.pontointeligente.enums.PerfilEnum
import com.ffsilva.pontointeligente.enums.TipoEnum
import com.ffsilva.pontointeligente.services.FuncionarioService
import com.ffsilva.pontointeligente.services.LancamentoService
import com.ffsilva.pontointeligente.utils.SenhaUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.*

@SpringBootTest(classes = [FuncionarioService::class, LancamentoService::class])
@AutoConfigureMockMvc
@RunWith(SpringRunner::class)
class LancamentoControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val funcionarioService: FuncionarioService? = null
    @MockBean
    private val lancamentoService: LancamentoService? = null

    private val urlBase: String = "/api/lancamentos/"
    private val idFuncionario: String = "1"
    private val idLancamento: String = "1"
    private val tipo: String = TipoEnum.INICIO_TRABALHO.name
    private val data: LocalDateTime = LocalDateTime.now()

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun testCadastrarLancamento() {
        val lancamento: Lancamento = obterDadosLancamento()

        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario)).willReturn(funcionario())
        BDDMockito.given(lancamentoService?.persistir(obterDadosLancamento())).willReturn(lancamento)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.tipo").value(tipo))
                .andExpect(jsonPath("$.data.data").value(data.toString()))
                .andExpect(jsonPath("$.data.funcionarioId").value(idFuncionario))
                .andExpect(jsonPath("$.erros").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun testCadastrarLancamentoFuncionarioIdInvalido() {
        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario)).willReturn(null)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.erros").value("Funcionário não encontrado. ID inexistente."))
                .andExpect(jsonPath("$.data").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser(username = "admin@admin.com", roles = arrayOf("ADMIN"))
    fun testRemoverLancamento() {
        BDDMockito.given(lancamentoService?.buscarPorId(idLancamento)).willReturn(Optional.of(obterDadosLancamento()))

        mvc!!.perform(MockMvcRequestBuilders.delete(urlBase + idLancamento)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
    }

    @Throws(JsonProcessingException::class)
    private fun obterJsonRequisicaoPost(): String {
        val lancamentoDto = LancamentoDto(
                data = data.toString(),
                tipo = tipo,
                descricao = "Descrição",
                localizacao = "1.234,4.234",
                funcionarioId = idFuncionario
        )

        return ObjectMapper().writeValueAsString(lancamentoDto)
    }

    private fun obterDadosLancamento(): Lancamento =
            Lancamento(
                    id = idLancamento,
                    data = data,
                    tipo = TipoEnum.valueOf(tipo),
                    descricao = "Descrição",
                    localizacao = "1.243,4.345",
                    funcionarioId = idFuncionario
            )

    private fun funcionario(): Funcionario =
            Funcionario(
                    id = idFuncionario,
                    cpf = "23145699876",
                    email = "email@email.com",
                    nome = "Nome",
                    senha = SenhaUtils().gerarBCrypt("123456"),
                    perfil = PerfilEnum.ROLE_USUARIO,

                    empresaId = "1",
                    valorHora = 20.00,
                    qtdHorasTrabalhoDia = 8f,
                    qtdHorasAlmoco = 1f
            )
}
