//referências aos elementos do DOM
const startDateInput = document.getElementById("startDate");
const endDateInput = document.getElementById("endDate");
const fetchButton = document.getElementById("fetchButton");
const loadingIndicator = document.getElementById("loading-indicator");
const errorMessageDiv = document.getElementById("error-message");
const resultsDiv = document.getElementById("results");

// URL base do backend
const BACKEND_URL = "http://localhost:8080/asteroids/summary";

function formatDate(date) {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0"); // Mês é 0-indexed
  const day = String(d.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

// datas padrão para os inputs
document.addEventListener("DOMContentLoaded", () => {
  const today = new Date();
  const tomorrow = new Date(today);
  tomorrow.setDate(today.getDate() + 1);

  startDateInput.value = formatDate(today);
  endDateInput.value = formatDate(tomorrow);
});

// listener do botão de busca
fetchButton.addEventListener("click", async () => {
  const startDate = startDateInput.value;
  const endDate = endDateInput.value;

  errorMessageDiv.textContent = "";
  errorMessageDiv.classList.add("hidden");
  resultsDiv.innerHTML = "";

  // Validação de datas (feedback imediato no frontend)
  if (!startDate || !endDate) {
    errorMessageDiv.textContent = "Por favor, selecione ambas as datas.";
    errorMessageDiv.classList.remove("hidden");
    return;
  }

  const start = new Date(startDate);
  const end = new Date(endDate);
  const diffTime = Math.abs(end - start);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  if (start > end) {
    errorMessageDiv.textContent =
      "A data inicial não pode ser posterior à data final.";
    errorMessageDiv.classList.remove("hidden");
    return;
  }
  if (diffDays > 7) {
    errorMessageDiv.textContent =
      "O intervalo de datas não pode exceder 7 dias.";
    errorMessageDiv.classList.remove("hidden");
    return;
  }

  // indicador de carregamento
  loadingIndicator.classList.remove("hidden");

  try {
    // URL completa com os parâmetros de data
    const url = `${BACKEND_URL}?start_date=${startDate}&end_date=${endDate}`;

    const response = await fetch(url);

    // bem-sucedida (status 2xx)
    if (!response.ok) {
      const errorBody = await response.json();
      throw new Error(
        `Erro ${response.status}: ${errorBody.message || response.statusText}`
      );
    }

    // Analisa a resposta JSON
    const asteroids = await response.json();

    loadingIndicator.classList.add("hidden");

    if (asteroids.length === 0) {
      resultsDiv.innerHTML =
        '<p class="text-gray-400">Nenhum asteroide encontrado para o período selecionado.</p>';
      return;
    }

    // Renderiza os asteroides na interface
    renderAsteroids(asteroids);
  } catch (error) {
    loadingIndicator.classList.add("hidden");
    errorMessageDiv.textContent = `Falha ao buscar dados: ${error.message}. Verifique se o backend está rodando e acessível.`;
    errorMessageDiv.classList.remove("hidden");
    console.error("Erro na requisição:", error);
  }
});

// renderizar a lista de asteroides
function renderAsteroids(asteroids) {
  resultsDiv.innerHTML = "";
  asteroids.forEach((asteroid) => {
    const asteroidCard = document.createElement("div");
    asteroidCard.className = "asteroid-card";

    const diameterMin = asteroid.estimatedDiameterKilometers
      ? asteroid.estimatedDiameterKilometers.estimated_diameter_min.toFixed(3)
      : "N/A";
    const diameterMax = asteroid.estimatedDiameterKilometers
      ? asteroid.estimatedDiameterKilometers.estimated_diameter_max.toFixed(3)
      : "N/A";

    asteroidCard.innerHTML = `
                    <h3>${asteroid.name}</h3>
                    <p><strong>Diâmetro Estimado (km):</strong> ${diameterMin} - ${diameterMax}</p>
                    <p><strong>Distância de Aproximação (km):</strong> ${parseFloat(
                      asteroid.missDistanceKilometers
                    ).toLocaleString("pt-BR")} km</p>
                    <p><strong>Velocidade Relativa (km/s):</strong> ${parseFloat(
                      asteroid.relativeVelocityKilometersPerSecond
                    ).toFixed(2)} km/s</p>
                    <p><strong>Potencialmente Perigoso:</strong> <span class="${
                      asteroid.potentiallyHazardousAsteroid ? "hazardous" : ""
                    }">${
      asteroid.potentiallyHazardousAsteroid ? "Sim" : "Não"
    }</span></p>
                `;
    resultsDiv.appendChild(asteroidCard);
  });
}
