// ============================================================
// Close To Us — closetous.js
// Lógica de negócio, integração com API NASA via backend,
// validação de datas e renderização dos resultados.
// ============================================================

// --- Referências ao DOM ---
const startDateInput     = document.getElementById("startDate");
const endDateInput       = document.getElementById("endDate");
const fetchButton        = document.getElementById("fetchButton");
const loadingIndicator   = document.getElementById("loading-indicator");
const errorMessageDiv    = document.getElementById("error-message");
const resultsDiv         = document.getElementById("results");
const resultsCountEl     = document.getElementById("results-count");
const resultsSectionEl   = document.getElementById("results-section");

// --- Configuração da API ---
const BACKEND_URL = "http://localhost:8080/api/v1/asteroids";

// --- Utilitários de data ---
function formatDate(date) {
  const d      = new Date(date);
  const year   = d.getFullYear();
  const month  = String(d.getMonth() + 1).padStart(2, "0");
  const day    = String(d.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function formatDateDisplay(dateStr) {
  if (!dateStr) return "—";
  // dateStr pode vir como "YYYY-MM-DD" ou outro formato do backend
  const parts = dateStr.split("-");
  if (parts.length === 3) {
    return `${parts[2]}/${parts[1]}/${parts[0]}`;
  }
  return dateStr;
}

// --- Estado da UI ---
function showLoading() {
  loadingIndicator.classList.remove("hidden");
  fetchButton.disabled  = true;
  fetchButton.classList.add("btn-loading");
}

function hideLoading() {
  loadingIndicator.classList.add("hidden");
  fetchButton.disabled = false;
  fetchButton.classList.remove("btn-loading");
}

function showError(message) {
  errorMessageDiv.innerHTML = `
    <div class="state-container error-state">
      <div class="error-icon" aria-hidden="true">⚠</div>
      <p class="state-label">${message}</p>
    </div>
  `;
  errorMessageDiv.classList.remove("hidden");
}

function clearError() {
  errorMessageDiv.textContent = "";
  errorMessageDiv.classList.add("hidden");
}

function clearResults() {
  resultsDiv.innerHTML            = "";
  resultsCountEl.textContent      = "";
  resultsSectionEl.classList.remove("has-results");
}

// --- Inicialização das datas padrão ---
document.addEventListener("DOMContentLoaded", () => {
  const today    = new Date();
  const tomorrow = new Date(today);
  tomorrow.setDate(today.getDate() + 1);

  startDateInput.value = formatDate(today);
  endDateInput.value   = formatDate(tomorrow);

  // Inicializar animação espacial (definida em space-animation.js)
  if (typeof initSpaceAnimation === "function") {
    initSpaceAnimation();
  }
});

// --- Listener do botão de busca ---
fetchButton.addEventListener("click", async () => {
  const startDate = startDateInput.value;
  const endDate   = endDateInput.value;

  clearError();
  clearResults();

  // Validação: campos obrigatórios
  if (!startDate || !endDate) {
    showError("Por favor, selecione ambas as datas para realizar a consulta.");
    return;
  }

  const start    = new Date(startDate);
  const end      = new Date(endDate);
  const diffTime = Math.abs(end - start);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  // Validação: ordem das datas
  if (start > end) {
    showError("A data inicial não pode ser posterior à data final.");
    return;
  }

  // Validação: limite de 7 dias da API NASA
  if (diffDays > 7) {
    showError("O intervalo de datas não pode exceder 7 dias (limitação da API NASA).");
    return;
  }

  showLoading();

  try {
    const url      = `${BACKEND_URL}?startDate=${startDate}&endDate=${endDate}`;
    const response = await fetch(url);

    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = response.statusText;
      try {
        const errorJson = JSON.parse(errorText);
        errorMessage = errorJson.message || errorMessage;
      } catch (e) {
        errorMessage = errorText || errorMessage;
      }
      throw new Error(`Erro ${response.status}: ${errorMessage}`);
    }

    const asteroids = await response.json();

    hideLoading();

    if (!Array.isArray(asteroids) || asteroids.length === 0) {
      showEmptyState();
      return;
    }

    renderAsteroids(asteroids);

  } catch (error) {
    hideLoading();
    showError(
      `Falha ao buscar dados: ${error.message}. ` +
      `Verifique se o backend está rodando em localhost:8080.`
    );
    console.error("Erro na requisição:", error);
  }
});

// --- Estado: sem resultados ---
function showEmptyState() {
  resultsDiv.innerHTML = `
    <div class="state-container empty-state">
      <p class="state-label">Nenhum objeto encontrado para o período selecionado.</p>
      <p class="state-hint">Tente selecionar um intervalo de datas diferente.</p>
    </div>
  `;
  resultsSectionEl.classList.add("has-results");
}

// --- Renderização dos asteroides ---
// Formato do DTO do backend (AsteroidResponse):
//   name         : string  — designação do objeto
//   maxMeters    : number  — diâmetro estimado máximo em metros
//   orbitingBody : string  — corpo celeste em órbita
//   isFast       : boolean — velocidade relativa alta
//   dangerous    : boolean — potencialmente perigoso (PHA)
function renderAsteroids(asteroids) {
  resultsDiv.innerHTML = "";

  // Atualizar contador
  resultsSectionEl.classList.add("has-results");
  resultsCountEl.textContent = `${asteroids.length} objeto${asteroids.length !== 1 ? "s" : ""} encontrado${asteroids.length !== 1 ? "s" : ""}`;

  asteroids.forEach((asteroid, index) => {
    const row = document.createElement("div");
    row.className   = "asteroid-card";
    row.setAttribute("role", "listitem");
    row.setAttribute("tabindex", "0");
    row.style.setProperty("--row-index", index);

    const name         = asteroid.name        || "Desconhecido";
    const maxDiameter  = (asteroid.maxMeters !== undefined && asteroid.maxMeters !== null)
      ? `${asteroid.maxMeters.toFixed(1)} m`
      : "N/A";
    const orbitingBody = asteroid.orbitingBody || "N/A";
    const isFast       = asteroid.isFast;
    const isDangerous  = asteroid.dangerous;

    // Indicador de velocidade
    const speedLabel   = isFast
      ? `<span class="tag tag--alert">Rápido</span>`
      : `<span class="tag">Nominal</span>`;

    // Indicador de perigo
    const dangerLabel  = isDangerous
      ? `<span class="hazardous" aria-label="Potencialmente perigoso">⚠ Perigoso</span>`
      : `<span class="safe" aria-label="Sem risco imediato">— Seguro</span>`;

    row.innerHTML = `
      <span class="asteroid-name" title="${name}">${name}</span>
      <span class="asteroid-data" data-label="Diâmetro">${maxDiameter}</span>
      <span class="asteroid-data" data-label="Velocidade">${speedLabel}</span>
      <span class="asteroid-data" data-label="Órbita">${orbitingBody}</span>
      <span class="asteroid-status" data-label="Status">${dangerLabel}</span>
    `;

    resultsDiv.appendChild(row);
  });
}
