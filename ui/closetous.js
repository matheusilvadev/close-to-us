// ============================================================
// Close To Us — closetous.js
// Lógica de negócio, integração com API NASA / Backend,
// validação e auditoria rigorosa de dados astronômicos.
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
  const d     = new Date(date);
  const year  = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day   = String(d.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

// --- Estado da UI ---
function showLoading() {
  loadingIndicator.classList.remove("hidden");
  fetchButton.disabled = true;
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
  resultsDiv.innerHTML = "";
  resultsCountEl.textContent = "";
  resultsSectionEl.classList.remove("has-results");
}

// --- Inicialização das datas padrão ---
document.addEventListener("DOMContentLoaded", () => {
  const today = new Date();
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
    const url = `${BACKEND_URL}?startDate=${startDate}&endDate=${endDate}`;
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

    const data = await response.json();
    hideLoading();

    // Normaliza tanto a resposta do backend (array DTO)
    // quanto uma resposta direta do feed da NASA (near_earth_objects map)
    const asteroids = normalizeApiResponse(data);

    if (!asteroids || asteroids.length === 0) {
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
    console.error("[CloseToUs] Erro na requisição:", error);
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

// --- Normalização robusta de resposta da API ---
function normalizeApiResponse(data) {
  if (Array.isArray(data)) {
    return data;
  }

  // Suporte a resposta direta da NASA NeoWs Feed
  if (data && typeof data === "object" && data.near_earth_objects) {
    const list = [];
    const dates = Object.keys(data.near_earth_objects).sort();
    for (const date of dates) {
      const dayObjects = data.near_earth_objects[date];
      if (Array.isArray(dayObjects)) {
        for (const item of dayObjects) {
          // Anexa a data do evento para seleção precisa da aproximação
          item._targetDate = date;
          list.push(item);
        }
      }
    }
    return list;
  }

  return [];
}

// --- Seleção e auditoria do registro de aproximação (Close Approach Data) ---
function extractApproachData(asteroid) {
  if (!asteroid.close_approach_data || !Array.isArray(asteroid.close_approach_data)) {
    return null;
  }

  const list = asteroid.close_approach_data;
  if (list.length === 0) return null;

  // Se o objeto possui data alvo associada, busca a aproximação exata daquela data
  if (asteroid._targetDate) {
    const match = list.find((a) => a && a.close_approach_date === asteroid._targetDate);
    if (match) return match;
  }

  // Fallback: primeiro registro de aproximação
  return list[0];
}

// --- Extração e formatação com auditoria de dados ---
function parseAsteroidData(asteroid) {
  // 1. Designação: prioriza asteroid.designation, fallback para asteroid.name
  let designation = "Desconhecido";
  if (asteroid.designation && typeof asteroid.designation === "string" && asteroid.designation.trim() !== "") {
    designation = asteroid.designation.trim();
  } else if (asteroid.name && typeof asteroid.name === "string" && asteroid.name.trim() !== "") {
    designation = asteroid.name.trim();
  }

  // 2. Diâmetro máximo (em metros)
  // Caminhos suportados:
  // - asteroid.maxDiameterMeters (DTO corrigido)
  // - asteroid.estimated_diameter.meters.estimated_diameter_max (NASA original)
  // - asteroid.maxMeters (legado)
  let rawDiameter = null;
  if (typeof asteroid.maxDiameterMeters === "number" && !isNaN(asteroid.maxDiameterMeters)) {
    rawDiameter = asteroid.maxDiameterMeters;
  } else if (
    asteroid.estimated_diameter &&
    asteroid.estimated_diameter.meters &&
    typeof asteroid.estimated_diameter.meters.estimated_diameter_max === "number" &&
    !isNaN(asteroid.estimated_diameter.meters.estimated_diameter_max)
  ) {
    rawDiameter = asteroid.estimated_diameter.meters.estimated_diameter_max;
  } else if (typeof asteroid.maxMeters === "number" && !isNaN(asteroid.maxMeters)) {
    rawDiameter = asteroid.maxMeters;
  }

  let formattedDiameter = "—";
  if (rawDiameter !== null && rawDiameter > 0) {
    formattedDiameter = `${rawDiameter.toLocaleString("pt-BR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })} m`;
  }

  // Identificação do registro de aproximação caso venha da NASA direta
  const approach = extractApproachData(asteroid);

  // 3. Velocidade relativa (em km/s)
  // Caminhos suportados:
  // - asteroid.velocityKmS (DTO backend corrigido)
  // - approach.relative_velocity.kilometers_per_second
  // - approach.relative_velocity.kilometers_per_hour (convertido: / 3600)
  let rawVelocity = null;
  if (typeof asteroid.velocityKmS === "number" && !isNaN(asteroid.velocityKmS) && asteroid.velocityKmS > 0) {
    rawVelocity = asteroid.velocityKmS;
  } else if (approach && approach.relative_velocity) {
    const kmSStr = approach.relative_velocity.kilometers_per_second;
    const kmHStr = approach.relative_velocity.kilometers_per_hour;

    if (kmSStr && !isNaN(parseFloat(kmSStr))) {
      rawVelocity = parseFloat(kmSStr);
    } else if (kmHStr && !isNaN(parseFloat(kmHStr))) {
      rawVelocity = parseFloat(kmHStr) / 3600;
    }
  }

  let formattedVelocity = "—";
  if (rawVelocity !== null && rawVelocity > 0) {
    formattedVelocity = `${rawVelocity.toLocaleString("pt-BR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })} km/s`;
  }

  // 4. Distância de aproximação (em km)
  // Caminhos suportados:
  // - asteroid.missDistanceKm (DTO backend corrigido)
  // - approach.miss_distance.kilometers
  let rawDistance = null;
  if (typeof asteroid.missDistanceKm === "number" && !isNaN(asteroid.missDistanceKm) && asteroid.missDistanceKm > 0) {
    rawDistance = asteroid.missDistanceKm;
  } else if (approach && approach.miss_distance && approach.miss_distance.kilometers) {
    const distStr = approach.miss_distance.kilometers;
    if (!isNaN(parseFloat(distStr))) {
      rawDistance = parseFloat(distStr);
    }
  }

  let formattedDistance = "—";
  if (rawDistance !== null && rawDistance > 0) {
    formattedDistance = `${rawDistance.toLocaleString("pt-BR", {
      maximumFractionDigits: 0,
    })} km`;
  }

  // 5. Corpo em órbita
  // Caminhos suportados:
  // - asteroid.orbitingBody
  // - approach.orbiting_body
  let orbitingBody = "—";
  if (asteroid.orbitingBody && typeof asteroid.orbitingBody === "string" && asteroid.orbitingBody !== "Unknown") {
    orbitingBody = asteroid.orbitingBody;
  } else if (approach && approach.orbiting_body) {
    orbitingBody = approach.orbiting_body;
  }

  // 6. Status de risco (estritamente booleano da NASA)
  // Caminhos suportados:
  // - asteroid.dangerous
  // - asteroid.is_potentially_hazardous_asteroid
  let isHazardous = false;
  if (typeof asteroid.dangerous === "boolean") {
    isHazardous = asteroid.dangerous;
  } else if (typeof asteroid.is_potentially_hazardous_asteroid === "boolean") {
    isHazardous = asteroid.is_potentially_hazardous_asteroid;
  }

  return {
    designation,
    formattedDiameter,
    formattedVelocity,
    formattedDistance,
    orbitingBody,
    isHazardous,
  };
}

// --- Renderização dos asteroides com design system monocromático ---
function renderAsteroids(asteroids) {
  resultsDiv.innerHTML = "";

  resultsSectionEl.classList.add("has-results");
  const count = asteroids.length;
  resultsCountEl.textContent = `${count} objeto${count !== 1 ? "s" : ""} registrado${count !== 1 ? "s" : ""}`;

  asteroids.forEach((asteroid, index) => {
    const parsed = parseAsteroidData(asteroid);

    const row = document.createElement("div");
    row.className = "asteroid-card";
    row.setAttribute("role", "listitem");
    row.setAttribute("tabindex", "0");
    row.style.setProperty("--row-index", index);

    const statusBadge = parsed.isHazardous
      ? `<span class="hazardous" aria-label="Potencialmente perigoso">Potencialmente perigoso</span>`
      : `<span class="safe" aria-label="Não potencialmente perigoso">— Não perigoso</span>`;

    row.innerHTML = `
      <span class="asteroid-name" title="${parsed.designation}">${parsed.designation}</span>
      <span class="asteroid-data" data-label="Diâmetro máx.">${parsed.formattedDiameter}</span>
      <span class="asteroid-data" data-label="Velocidade">${parsed.formattedVelocity}</span>
      <span class="asteroid-data" data-label="Distância">${parsed.formattedDistance}</span>
      <span class="asteroid-data" data-label="Corpo em órbita">${parsed.orbitingBody}</span>
      <span class="asteroid-status" data-label="Status">${statusBadge}</span>
    `;

    resultsDiv.appendChild(row);
  });
}
