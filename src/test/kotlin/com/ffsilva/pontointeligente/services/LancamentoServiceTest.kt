package com.ffsilva.pontointeligente.services

import com.ffsilva.pontointeligente.documents.Lancamento
import com.ffsilva.pontointeligente.enums.TipoEnum
import com.ffsilva.pontointeligente.repostories.LancamentoRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@RunWith(SpringRunner::class)
@SpringBootTest
class LancamentoServiceTest {

    @MockBean
    private val lancamentoRepository: LancamentoRepository? = null

    @Autowired
    private val lancamentoService: LancamentoService? = null

    private val id: String = "1"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given<Page<Lancamento>>(lancamentoRepository?.findByFuncionarioId(id, PageRequest.of(0, 10)))
                .willReturn(PageImpl(ArrayList<Lancamento>()))
        BDDMockito.given(lancamentoRepository?.findById(id)).willReturn(Optional.of(lancamento()))
        BDDMockito.given(lancamentoRepository?.save(Mockito.any(Lancamento::class.java)))
                .willReturn(lancamento())
    }

    @Test
    fun testBuscarLancamnetoPorFuncionarioId() {
        val lancamento: Page<Lancamento>? = lancamentoService?.buscarPorFuncionarioId(id, PageRequest.of(0, 10))
        Assert.assertNotNull(lancamento)
    }

    @Test
    fun testBuscarLancamentoPorId() {
        val lancamento: Lancamento? = lancamentoService?.buscarPorId(id)
        Assert.assertNotNull(lancamento)
    }

    @Test
    fun testPeristirLancamento() {
        val lancamento: Lancamento? = lancamentoService?.persistir(lancamento())
        Assert.assertNotNull(lancamento)
    }

    private fun lancamento(): Lancamento = Lancamento(
            id = id,
            data = LocalDateTime.now(),
            tipo = TipoEnum.INICIO_ALMOCO,
            funcionarioId = "1",
            descricao = "Teste lancameno service",
            localizacao = "teste"
    )
}