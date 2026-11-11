package com.AdhanSouza.pergamo // Pacote oficial do projeto — NÃO ALTERAR

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// 1. PALETA DE CORES — TEMA ESCURO CLÁSSICO GRECO-ROMANO (Biblioteca de Pérgamo)
//    Todas as cores são definidas aqui de forma centralizada para facilitar
//    a manutenção e garantir consistência visual em toda a interface.
// ============================================================================

/** Fundo principal: Azul Noturno Profundo — evoca o céu da Antiguidade Clássica */
val FundoPrincipal = Color(0xFF0F1E36)

/** Fundo de cards e superfícies: Azul Acrópole */
val FundoCard = Color(0xFF162A4A)

/** Fundo de destaque, modais e seções especiais: Azul Imperial */
val FundoDestaque = Color(0xFF1E365D)

/** Dourado Clássico / Ocre Imperial — botões, bordas, selos e destaques principais */
val DouradoClassico = Color(0xFFD4A72C)

/** Dourado Suave — divisores e bordas sutis (alpha 30%) */
val DouradoSuave = Color(0x4DD4A72C)

/** Texto Principal: Branco Mármore */
val TextoMarmore = Color(0xFFF8FAFC)

/** Texto Secundário: Cinza Prateado */
val TextoPrata = Color(0xFF94A3B8)

// ============================================================================
// 2. MODELO DE DADOS — representa a entidade "Livro" do acervo
//    Corresponde à tabela 'livros' projetada no banco de dados MySQL.
// ============================================================================
data class Livro(
    val id: Int,
    val titulo: String,
    val autor: String,
    val genero: String,
    val ano: String,         // String para suportar datas como "380 a.C."
    val descricao: String,
    val corCapa: Color       // Cor temática que representa visualmente a capa
)

// ============================================================================
// 3. PORTA DE ENTRADA DO APLICATIVO — ciclo de vida Android (ComponentActivity)
//    O método onCreate() é chamado pelo sistema ao iniciar o app.
// ============================================================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent substitui o antigo sistema de XML layouts pelo Jetpack Compose
        setContent {
            // MaterialTheme aplica o sistema de design Material 3 globalmente
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FundoPrincipal
                ) {
                    // Componente raiz que gerencia toda a navegação do app
                    AppBibliotecaPergamo()
                }
            }
        }
    }
}

// ============================================================================
// 4. COMPONENTE RAIZ — gerencia o estado de navegação entre as abas
//    Utiliza Scaffold para estruturar: cabeçalho fixo + área de conteúdo
// ============================================================================
@Composable
fun AppBibliotecaPergamo() {
    // Estado reativo que controla qual aba está visível no momento
    var abaAtual by remember { mutableStateOf("acervo") }

    Scaffold(
        topBar = {
            // Cabeçalho fixo com monograma, título clássico e barra de abas
            CabecalhoPergamo(
                abaSelecionada = abaAtual,
                aoTrocarAba = { novaAba -> abaAtual = novaAba }
            )
        },
        containerColor = FundoPrincipal
    ) { espacamentoInterno ->
        // Box garante que o conteúdo não fique atrás do cabeçalho fixo
        Box(modifier = Modifier.padding(espacamentoInterno)) {
            // Troca de tela conforme a aba ativa
            when (abaAtual) {
                "acervo"     -> TelaAcervo(aoNavegar = { abaAtual = it })
                "sobre"      -> TelaSobre()
                "inscrever"  -> TelaInscricao()
            }
        }
    }
}

// ============================================================================
// 5. CABEÇALHO CLÁSSICO GRECO-ROMANO (RF06)
//    Contém: Selo monograma "Π", título "PÉRGAMO" em Serif, subtítulo com
//    linhas douradas horizontais e a barra de navegação com 3 abas.
// ============================================================================
@Composable
fun CabecalhoPergamo(abaSelecionada: String, aoTrocarAba: (String) -> Unit) {
    Surface(
        color = FundoPrincipal,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 14.dp, start = 16.dp, end = 16.dp)
        ) {
            // --- Linha do monograma + título ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Selo circular com borda dourada contendo o monograma "Π"
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(2.dp, DouradoClassico, CircleShape)
                        .background(FundoDestaque),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Π",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DouradoClassico
                    )
                }

                // Bloco de título e slogan
                Column {
                    Text(
                        text = "PÉRGAMO",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        letterSpacing = 2.sp,
                        color = TextoMarmore
                    )
                    // Subtítulo clássico flanqueado por linhas douradas sutis
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Linha dourada à esquerda do subtítulo
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(1.dp)
                                .background(DouradoClassico.copy(alpha = 0.6f))
                        )
                        Text(
                            text = "LEITURA SEM FRONTEIRAS",
                            fontSize = 10.sp,
                            letterSpacing = 2.5.sp,
                            color = TextoPrata,
                            fontWeight = FontWeight.Light
                        )
                        // Linha dourada à direita do subtítulo
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(1.dp)
                                .background(DouradoClassico.copy(alpha = 0.6f))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Divisor dourado sutil entre título e abas ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(DouradoSuave)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Barra de navegação com 3 abas ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotaoAba(
                    texto = "Acervo",
                    ativo = abaSelecionada == "acervo",
                    modifier = Modifier.weight(1f),
                    aoClicar = { aoTrocarAba("acervo") }
                )
                BotaoAba(
                    texto = "Sobre",
                    ativo = abaSelecionada == "sobre",
                    modifier = Modifier.weight(1f),
                    aoClicar = { aoTrocarAba("sobre") }
                )
                BotaoAba(
                    texto = "Inscrever-se",
                    ativo = abaSelecionada == "inscrever",
                    modifier = Modifier.weight(1f),
                    aoClicar = { aoTrocarAba("inscrever") }
                )
            }
        }
    }
}

// ============================================================================
// 6. BOTÃO DE ABA — componente reutilizável para a navegação do cabeçalho
//    Aba ativa: fundo Dourado com texto escuro em negrito
//    Aba inativa: fundo transparente com borda dourada/prateada
// ============================================================================
@Composable
fun BotaoAba(
    texto: String,
    ativo: Boolean,
    modifier: Modifier = Modifier,
    aoClicar: () -> Unit
) {
    if (ativo) {
        // Aba ativa: preenchida com Dourado Clássico
        Button(
            onClick = aoClicar,
            modifier = modifier.height(36.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DouradoClassico,
                contentColor = Color(0xFF0F1E36)
            ),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Text(texto, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    } else {
        // Aba inativa: borda fina dourada/prateada, fundo transparente
        OutlinedButton(
            onClick = aoClicar,
            modifier = modifier.height(36.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoPrata),
            border = androidx.compose.foundation.BorderStroke(1.dp, DouradoSuave),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Text(texto, fontSize = 12.sp, fontWeight = FontWeight.Normal)
        }
    }
}

// ============================================================================
// 7. TELA 1 — ACERVO / CATÁLOGO (UC01, UC02, UC03)
//    Contém: Hero Banner, Card Destaque do Mês, busca + filtros e lista LazyColumn
// ============================================================================
@Composable
fun TelaAcervo(aoNavegar: (String) -> Unit) {
    // Gêneros disponíveis para filtro (UC02)
    val generos = listOf("Todos", "Clássicos", "Filosofia", "Ficção", "Tecnologia", "História")

    // Estados reativos — Compose recompõe automaticamente ao mudar estes valores
    var textoBusca by remember { mutableStateOf("") }
    var generoSelecionado by remember { mutableStateOf("Todos") }
    var livroSelecionado by remember { mutableStateOf<Livro?>(null) }

    // Acervo em memória com as 6 obras exigidas pelo protótipo
    val acervo = remember {
        listOf(
            Livro(
                id = 1,
                titulo = "Dom Casmurro",
                autor = "Machado de Assis",
                genero = "Clássicos",
                ano = "1899",
                descricao = "Narrado em primeira pessoa por Bentinho — o Dom Casmurro — o romance investiga com maestria a memória, o ciúme e a dúvida. Capitu traiu ou não traiu? A ambiguidade deliberada de Machado de Assis faz desta obra um dos maiores enigmas da literatura brasileira.",
                corCapa = Color(0xFF7C3AED)
            ),
            Livro(
                id = 2,
                titulo = "A República",
                autor = "Platão",
                genero = "Filosofia",
                ano = "380 a.C.",
                descricao = "Diálogos em que Sócrates e seus interlocutores investigam a natureza da justiça, do Estado ideal e da alma. Platão apresenta a Alegoria da Caverna, fundamento do pensamento filosófico ocidental sobre realidade e conhecimento.",
                corCapa = Color(0xFF0369A1)
            ),
            Livro(
                id = 3,
                titulo = "Meditações",
                autor = "Marco Aurélio",
                genero = "Filosofia",
                ano = "180 d.C.",
                descricao = "Diário íntimo do imperador-filósofo, escrito em grego como exercício espiritual. Reúne reflexões estoicas sobre virtude, transitoriedade e dever, revelando um homem que tentava, diariamente, ser melhor do que sua posição exigia.",
                corCapa = Color(0xFFB45309)
            ),
            Livro(
                id = 4,
                titulo = "Ilíada",
                autor = "Homero",
                genero = "Clássicos",
                ano = "750 a.C.",
                descricao = "Épico grego que narra cinquenta dias da Guerra de Troia, centrado na cólera de Aquiles e suas consequências. Fundadora da tradição literária ocidental, a Ilíada explora temas universais: honra, destino, guerra e mortalidade.",
                corCapa = Color(0xFF047857)
            ),
            Livro(
                id = 5,
                titulo = "Código Limpo",
                autor = "Robert C. Martin",
                genero = "Tecnologia",
                ano = "2009",
                descricao = "O 'Tio Bob' reúne décadas de experiência para ensinar como escrever código legível, testável e manutenível. Um manual imprescindível para desenvolvedores que desejam elevar a qualidade do seu trabalho além do funcionamento básico.",
                corCapa = Color(0xFF1D4ED8)
            ),
            Livro(
                id = 6,
                titulo = "Duna",
                autor = "Frank Herbert",
                genero = "Ficção",
                ano = "1965",
                descricao = "No planeta deserto Arrakis, Paul Atreides enfrenta traição, política intergaláctica e profecias messiânicas. Considerada a obra de ficção científica mais vendida de todos os tempos, Duna aborda ecologia, religião e poder com profundidade ímpar.",
                corCapa = Color(0xFFD97706)
            )
        )
    }

    // Filtragem reativa: combina gênero selecionado + texto digitado (UC02)
    val livrosFiltrados = acervo.filter { livro ->
        val bateGenero = generoSelecionado == "Todos" || livro.genero == generoSelecionado
        val bateBusca = textoBusca.isBlank() ||
                livro.titulo.contains(textoBusca, ignoreCase = true) ||
                livro.autor.contains(textoBusca, ignoreCase = true)
        bateGenero && bateBusca
    }

    // LazyColumn como container principal para evitar que o Hero Banner seja scrollável separadamente
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoPrincipal),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {

        // --- HERO BANNER ---
        item {
            HeroBanner(aoNavegar = aoNavegar)
        }

        // --- CARD DESTAQUE DO MÊS ---
        item {
            CardDestaqueMes(
                livro = acervo.first { it.id == 1 }, // Dom Casmurro como destaque
                aoClicar = { livroSelecionado = it }
            )
        }

        // --- BUSCA E FILTROS (UC02) ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Título da seção de catálogo
                Text(
                    text = "CATÁLOGO COMPLETO",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 1.5.sp,
                    color = DouradoClassico
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de busca com cores adaptadas ao tema escuro
                OutlinedTextField(
                    value = textoBusca,
                    onValueChange = { textoBusca = it },
                    placeholder = {
                        Text(
                            "Buscar por título ou autor...",
                            color = TextoPrata,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DouradoClassico,
                        unfocusedBorderColor = DouradoSuave,
                        focusedTextColor = TextoMarmore,
                        unfocusedTextColor = TextoMarmore,
                        cursorColor = DouradoClassico,
                        focusedContainerColor = FundoCard,
                        unfocusedContainerColor = FundoCard
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Chips de filtro por gênero com rolagem horizontal
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    generos.forEach { genero ->
                        val selecionado = generoSelecionado == genero
                        FilterChip(
                            selected = selecionado,
                            onClick = { generoSelecionado = genero },
                            label = {
                                Text(
                                    text = genero,
                                    fontSize = 12.sp,
                                    fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DouradoClassico,
                                selectedLabelColor = Color(0xFF0F1E36),
                                containerColor = FundoCard,
                                labelColor = TextoPrata
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = DouradoSuave,
                                selectedBorderColor = DouradoClassico,
                                enabled = true,
                                selected = selecionado
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Contador de resultados da busca atual
                Text(
                    text = "${livrosFiltrados.size} obra(s) encontrada(s)",
                    fontSize = 12.sp,
                    color = TextoPrata,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        // --- LISTA DE OBRAS (UC01) com key para otimização de performance ---
        items(livrosFiltrados, key = { it.id }) { livro ->
            CardLivro(
                livro = livro,
                aoClicar = { livroSelecionado = livro },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }

    // --- MODAL DE DETALHES (UC03) — exibido sobre a tela ao selecionar um livro ---
    livroSelecionado?.let { livro ->
        ModalDetalhesLivro(
            livro = livro,
            aoFechar = { livroSelecionado = null }
        )
    }
}

// ============================================================================
// 8. HERO BANNER — seção de boas-vindas com título anotado e dois botões de ação
// ============================================================================
@Composable
fun HeroBanner(aoNavegar: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                // Gradiente vertical do azul imperial para o fundo principal
                Brush.verticalGradient(
                    colors = listOf(FundoDestaque, FundoPrincipal)
                )
            )
            .border(
                width = 1.dp,
                color = DouradoSuave,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            // Rótulo clássico acima do título principal
            Text(
                text = "✦  ACERVO DIGITAL  ✦",
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = DouradoClassico,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Título principal com texto anotado (buildAnnotatedString):
            // "O Conhecimento" (Branco, Bold) + "ao Alcance" (Dourado, Itálico) + "de Todos" (Branco, Bold)
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextoMarmore, fontWeight = FontWeight.Bold)) {
                        append("O Conhecimento ")
                    }
                    withStyle(
                        SpanStyle(
                            color = DouradoClassico,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif
                        )
                    ) {
                        append("ao Alcance")
                    }
                    withStyle(SpanStyle(color = TextoMarmore, fontWeight = FontWeight.Bold)) {
                        append(" de Todos")
                    }
                },
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtítulo descritivo
            Text(
                text = "Explore nossa vasta coleção de livros, periódicos e recursos digitais. Faça sua inscrição gratuitamente e tenha acesso ilimitado ao saber.",
                fontSize = 14.sp,
                color = TextoPrata,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Dois botões de ação lado a lado
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Botão primário preenchido em Dourado
                Button(
                    onClick = { /* já está na tela de acervo */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DouradoClassico,
                        contentColor = Color(0xFF0F1E36)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Explorar Acervo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                // Botão secundário com borda clara (navega para inscrição)
                OutlinedButton(
                    onClick = { aoNavegar("inscrever") },
                    border = androidx.compose.foundation.BorderStroke(1.dp, TextoMarmore.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoMarmore),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Inscrever-se", fontSize = 13.sp)
                }
            }
        }
    }
}

// ============================================================================
// 9. CARD DESTAQUE DO MÊS — seção especial com borda dourada e etiqueta em caixa alta
// ============================================================================
@Composable
fun CardDestaqueMes(livro: Livro, aoClicar: (Livro) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Título da seção
        Text(
            text = "EM DESTAQUE",
            fontSize = 11.sp,
            letterSpacing = 2.sp,
            color = DouradoClassico,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card com borda dourada destacada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(FundoDestaque)
                .border(1.5.dp, DouradoClassico, RoundedCornerShape(12.dp))
                .clickable { aoClicar(livro) }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Etiqueta "DESTAQUE DO MÊS" em caixa alta e dourado
                Surface(
                    color = DouradoClassico.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "✦  DESTAQUE DO MÊS  ✦",
                        color = DouradoClassico,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Miniatura da capa clássica com moldura dourada e iniciais em Serif
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(90.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, DouradoClassico, RoundedCornerShape(8.dp))
                            .background(livro.corCapa),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = livro.titulo.take(2).uppercase(),
                            fontFamily = FontFamily.Serif,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // Informações textuais do destaque
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = livro.titulo,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TextoMarmore
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${livro.autor} • ${livro.ano}",
                            fontSize = 13.sp,
                            color = TextoPrata
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toque para ler a sinopse →",
                            fontSize = 12.sp,
                            color = DouradoClassico,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// 10. CARD DE LIVRO — item reutilizável da lista LazyColumn (UC01)
//     Inclui: miniatura com iniciais em Serif, badge de gênero dourado,
//     título em Serif, autor, ano e indicação de toque para sinopse.
// ============================================================================
@Composable
fun CardLivro(livro: Livro, aoClicar: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(FundoCard)
            .border(1.dp, DouradoSuave, RoundedCornerShape(12.dp))
            .clickable { aoClicar() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Miniatura vertical simulando capa clássica com moldura e iniciais em Serif
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.5.dp, DouradoClassico.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .background(livro.corCapa),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = livro.titulo.take(2).uppercase(),
                    fontFamily = FontFamily.Serif,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Coluna de informações textuais da obra
            Column(modifier = Modifier.weight(1f)) {
                // Badge dourado do gênero
                Surface(
                    color = DouradoClassico.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = livro.genero.uppercase(),
                        color = DouradoClassico,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Título em FontFamily.Serif conforme protótipo clássico
                Text(
                    text = livro.titulo,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoMarmore,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Autor e ano em cinza prateado
                Text(
                    text = "${livro.autor} • ${livro.ano}",
                    fontSize = 13.sp,
                    color = TextoPrata
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Indicação de interação
                Text(
                    text = "Toque para ler a sinopse →",
                    fontSize = 12.sp,
                    color = DouradoClassico,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ============================================================================
// 11. MODAL DE DETALHES (UC03) — AlertDialog com tema escuro clássico
//     Exibe: título em Serif, autor/ano em prata, tag dourada de gênero,
//     sinopse completa e botão dourado "Fechar".
// ============================================================================
@Composable
fun ModalDetalhesLivro(livro: Livro, aoFechar: () -> Unit) {
    AlertDialog(
        onDismissRequest = aoFechar,
        containerColor = FundoCard,
        // Borda dourada no modal de detalhes
        shape = RoundedCornerShape(16.dp),
        title = {
            Column {
                // Título da obra em FontFamily.Serif e cor branca (mármore)
                Text(
                    text = livro.titulo,
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoMarmore
                )
                Spacer(modifier = Modifier.height(2.dp))
                // Autor e ano em cinza prateado
                Text(
                    text = "${livro.autor} • ${livro.ano}",
                    fontSize = 13.sp,
                    color = TextoPrata
                )
            }
        },
        text = {
            Column {
                // Divisor dourado sutil
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(DouradoSuave)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tag dourada de gênero
                Surface(
                    color = DouradoClassico.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = livro.genero.uppercase(),
                        color = DouradoClassico,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rótulo "Sinopse" em dourado
                Text(
                    text = "Sinopse",
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DouradoClassico
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Texto completo da sinopse
                Text(
                    text = livro.descricao,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = TextoMarmore
                )
            }
        },
        confirmButton = {
            // Botão dourado de fechar o modal
            Button(
                onClick = aoFechar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DouradoClassico,
                    contentColor = Color(0xFF0F1E36)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Fechar", fontWeight = FontWeight.Bold)
            }
        }
    )
}

// ============================================================================
// 12. TELA 2 — SOBRE A BIBLIOTECA (UC05 — Alinhada à esquerda)
//     Exibe 3 Cards com histórico, missão e arquitetura do projeto.
// ============================================================================
@Composable
fun TelaSobre() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoPrincipal)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start // Alinhado à esquerda conforme UC05
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Rótulo clássico de seção
        Text(
            text = "INSTITUCIONAL",
            fontSize = 11.sp,
            letterSpacing = 2.sp,
            color = DouradoClassico,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Título principal da tela em Serif
        Text(
            text = "Sobre a Biblioteca Pérgamo",
            fontFamily = FontFamily.Serif,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextoMarmore
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- Card 1: O Legado de Pérgamo ---
        CardSobre(
            titulo = "O Legado de Pérgamo",
            conteudo = "A antiga Biblioteca de Pérgamo, fundada no século III a.C. na cidade de Pérgamo (atual Turquia), rivaliz" +
                    "ou com a célebre Biblioteca de Alexandria. Com um acervo estimado em mais de 200.000 volumes, foi um dos" +
                    " maiores centros de preservação do saber na Antiguidade Clássica.\n\nFoi em Pérgamo que se consolidou " +
                    "o uso do pergaminho — feito de pele de animal — como suporte para a escrita, em resposta ao bloqueio " +
                    "egípcio do papiro. Este material tornou-se a base dos livros medievais e fundou a tradição da " +
                    "encadernação ocidental."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // --- Card 2: Missão — Leitura sem Fronteiras ---
        CardSobre(
            titulo = "Missão: Leitura sem Fronteiras",
            conteudo = "A Biblioteca Pérgamo é uma plataforma digital criada para democratizar o acesso à leitura e à " +
                    "cultura. Inspirada no legado de Pérgamo, acreditamos que o conhecimento não deve ser restrito por " +
                    "geografia, renda ou disponibilidade física de espaços.\n\nPor meio de um aplicativo mobile acessível " +
                    "e intuitivo, colocamos o acervo literário na palma da mão de qualquer leitor, em qualquer lugar do " +
                    "mundo — gratuitamente."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // --- Card 3: Arquitetura e Recursos (MVP) ---
        CardSobre(
            titulo = "Arquitetura e Recursos (MVP)",
            conteudo = "• Linguagem: Kotlin com Jetpack Compose (Material 3)\n" +
                    "• UI: Interface declarativa 100% nativa para Android\n" +
                    "• Backend (planejado): API REST com autenticação JWT\n" +
                    "• Banco de dados: MySQL para gerenciar acervo e leitores\n" +
                    "• Funcionalidades MVP: catálogo com busca e filtro, sinopse por obra, cadastro de leitor e navegação por abas"
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Componente auxiliar para os Cards da tela Sobre (evita repetição de código)
@Composable
fun CardSobre(titulo: String, conteudo: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(FundoCard)
            .border(1.dp, DouradoSuave, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título do card em Serif e Dourado Clássico
            Text(
                text = titulo,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DouradoClassico
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Divisor dourado sutil entre título e conteúdo
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(1.dp)
                    .background(DouradoClassico.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Texto do card em Branco Mármore com boa legibilidade
            Text(
                text = conteudo,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = TextoMarmore
            )
        }
    }
}

// ============================================================================
// 13. TELA 3 — INSCREVER-SE / CADASTRO (UC04)
//     Formulário com Nome, E-mail e Senha (oculta), validação e feedback.
// ============================================================================
@Composable
fun TelaInscricao() {
    // Estados reativos do formulário
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemStatus by remember { mutableStateOf("") }
    var erroValidacao by remember { mutableStateOf(false) }
    var cadastroRealizado by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoPrincipal)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Rótulo clássico de seção
        Text(
            text = "ÁREA DO LEITOR",
            fontSize = 11.sp,
            letterSpacing = 2.sp,
            color = DouradoClassico,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Título da tela em Serif
        Text(
            text = "Inscrição Gratuita de Leitor",
            fontFamily = FontFamily.Serif,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextoMarmore
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Preencha os dados abaixo para acessar o acervo completo da Biblioteca Pérgamo.",
            fontSize = 14.sp,
            color = TextoPrata,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card do formulário com borda dourada suave
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(FundoCard)
                .border(1.dp, DouradoSuave, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Campo: Nome completo
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome completo", color = TextoPrata) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = campoTextoCores(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: E-mail
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail", color = TextoPrata) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = campoTextoCores(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Senha — caracteres ocultos com PasswordVisualTransformation
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha (mín. 6 caracteres)", color = TextoPrata) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp),
                    colors = campoTextoCores(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botão dourado de inscrição com validação dos campos
                Button(
                    onClick = {
                        // Regras de validação: campos não podem estar em branco e senha ≥ 6 caracteres
                        when {
                            nome.isBlank() -> {
                                erroValidacao = true
                                mensagemStatus = "Por favor, informe seu nome completo."
                            }
                            email.isBlank() || !email.contains("@") -> {
                                erroValidacao = true
                                mensagemStatus = "Por favor, informe um e-mail válido."
                            }
                            senha.length < 6 -> {
                                erroValidacao = true
                                mensagemStatus = "A senha deve ter no mínimo 6 caracteres."
                            }
                            else -> {
                                // Cadastro bem-sucedido
                                erroValidacao = false
                                cadastroRealizado = true
                                mensagemStatus = "Bem-vindo(a), $nome! Sua inscrição foi realizada com sucesso."
                                nome = ""
                                email = ""
                                senha = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DouradoClassico,
                        contentColor = Color(0xFF0F1E36)
                    )
                ) {
                    Text(
                        "Inscrever-se",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }

                // Exibe mensagem de sucesso ou erro após tentativa de inscrição
                if (mensagemStatus.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = if (erroValidacao)
                            Color(0xFFDC2626).copy(alpha = 0.12f)
                        else
                            DouradoClassico.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = mensagemStatus,
                            color = if (erroValidacao) Color(0xFFFF6B6B) else DouradoClassico,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ============================================================================
// 14. FUNÇÃO AUXILIAR — cores padronizadas para OutlinedTextField no tema escuro
//     Centraliza a configuração das cores dos campos de texto do formulário.
// ============================================================================
@Composable
fun campoTextoCores() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DouradoClassico,
    unfocusedBorderColor = DouradoSuave,
    focusedLabelColor = DouradoClassico,
    unfocusedLabelColor = TextoPrata,
    focusedTextColor = TextoMarmore,
    unfocusedTextColor = TextoMarmore,
    cursorColor = DouradoClassico,
    focusedContainerColor = FundoDestaque,
    unfocusedContainerColor = FundoCard
)
