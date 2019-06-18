package com.ffsilva.pontointeligente.services

import com.ffsilva.pontointeligente.documents.Empresa
import com.ffsilva.pontointeligente.repostories.EmpresaRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class EmpresaServiceTest {

    @Autowired
    private val empresaService: EmpresaService? = null

    @MockBean
    private val empresaRepository: EmpresaRepository? = null

    private val CNPJ = "5146365000100"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(empresaRepository?.findByCnpj(CNPJ)).willReturn(Optional.of(empresa()))
        BDDMockito.given(empresaRepository?.save(empresa())).willReturn(empresa())
    }

    @Test
    fun testBuscarEmpresaPorCnpj() {
        val empresa: Optional<Empresa> = empresaService?.buscarPorCnpj(CNPJ)!!
        Assert.assertTrue(empresa.isPresent)
    }

    @Test
    fun testPersistirEmpresa() {
        val empresa: Empresa? = empresaService?.persistir(empresa())
        Assert.assertNotNull(empresa)
    }

    private fun empresa(): Empresa = Empresa(id = "1", razaoSocial = "Razão Social", cnpj = CNPJ)
}