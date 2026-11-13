/**
 * space-animation.js
 * Cinematic monochromatic space animation for Close To Us.
 * Renders Earth as a central static element with asteroids on elliptical orbits,
 * a starfield, and floating particles — all on a Canvas 2D context.
 */

(function () {
  'use strict';

  // ─────────────────────────────────────────────
  // State
  // ─────────────────────────────────────────────
  let canvas, ctx;
  let width, height, cx, cy; // canvas logical dimensions and centre
  let stars = [];
  let floatingParticles = [];
  let orbits = [];
  let asteroids = [];
  let animFrameId = null;
  let lastTime = 0;

  // Orbit definitions: { angle (tilt in radians), scaleY, radiusX }
  // radiusX is set proportionally after resize.
  const ORBIT_CONFIGS = [
    { tiltDeg: 0,   scaleY: 0.30 },
    { tiltDeg: 35,  scaleY: 0.35 },
    { tiltDeg: 65,  scaleY: 0.25 },
    { tiltDeg: 110, scaleY: 0.45 },
    { tiltDeg: 145, scaleY: 0.40 },
    { tiltDeg: 80,  scaleY: 0.50 },
  ];

  const ASTEROID_COUNT = 10;
  const STAR_COUNT = 180;
  const FLOAT_COUNT = 18;

  // ─────────────────────────────────────────────
  // Helpers
  // ─────────────────────────────────────────────

  function rand(min, max) {
    return min + Math.random() * (max - min);
  }

  function randInt(min, max) {
    return Math.floor(rand(min, max + 1));
  }

  /**
   * Convert an orbit-local angle to canvas (x, y) coordinates.
   * Applies the same rotation + y-scale transform as when drawing the orbit.
   * @param {object} orbit  - orbit definition with tilt, scaleY, radiusX
   * @param {number} angle  - current angle in orbit (radians)
   * @returns {{ x: number, y: number }}
   */
  function orbitToCanvas(orbit, angle) {
    // Local ellipse point (before tilt rotation)
    const lx = Math.cos(angle) * orbit.radiusX;
    const ly = Math.sin(angle) * orbit.radiusX * orbit.scaleY;

    // Rotate by orbit tilt
    const cos = Math.cos(orbit.tilt);
    const sin = Math.sin(orbit.tilt);
    return {
      x: cx + lx * cos - ly * sin,
      y: cy + lx * sin + ly * cos,
    };
  }

  /**
   * Determine whether an asteroid is in front of the Earth.
   * Uses the Z component of the rotated ellipse position.
   * @param {object} orbit
   * @param {number} angle
   * @returns {boolean}
   */
  function isInFront(orbit, angle) {
    // The "depth" is the y-component of the un-scaled local point projected
    // onto the orbit plane's normal. A simpler approximation that works well:
    // compute the y-component of the orbit plane at this angle (before tilt).
    // Positive sin(angle) means the asteroid is coming toward the viewer.
    const rawY = Math.sin(angle); // positive → front half of ellipse
    // Additionally account for the tilt: a horizontal orbit (tilt≈0 or ≈PI/2)
    // separates front/back purely by sin(angle).
    return rawY > 0;
  }

  // ─────────────────────────────────────────────
  // Initialisation helpers
  // ─────────────────────────────────────────────

  function computeDimensions() {
    const dpr = window.devicePixelRatio || 1;
    const parent = canvas.parentElement || document.documentElement;
    const displayW = parent.clientWidth  || 800;
    const displayH = parent.clientHeight || 600;

    canvas.style.width  = displayW + 'px';
    canvas.style.height = displayH + 'px';
    canvas.width  = Math.round(displayW * dpr);
    canvas.height = Math.round(displayH * dpr);

    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

    width  = displayW;
    height = displayH;
    cx     = width  / 2;
    cy     = height / 2;
  }

  function earthRadius() {
    return Math.min(width, height) * 0.18;
  }

  /** Build orbit objects with computed radiusX values. */
  function initOrbits() {
    const baseRadius = earthRadius();
    orbits = ORBIT_CONFIGS.map((cfg) => ({
      tilt:    (cfg.tiltDeg * Math.PI) / 180,
      scaleY:  cfg.scaleY,
      radiusX: baseRadius * rand(1.55, 2.6),
    }));
  }

  function initStars() {
    stars = [];
    for (let i = 0; i < STAR_COUNT; i++) {
      stars.push({
        x:       rand(0, width),
        y:       rand(0, height),
        r:       rand(0.3, 1.2),
        baseOp:  rand(0.1, 0.5),
        phase:   rand(0, Math.PI * 2),
        speed:   rand(0.0003, 0.001),
      });
    }
  }

  function initFloatingParticles() {
    floatingParticles = [];
    for (let i = 0; i < FLOAT_COUNT; i++) {
      floatingParticles.push({
        x:   rand(0, width),
        y:   rand(0, height),
        vx:  rand(-0.08, 0.08),
        vy:  rand(-0.08, 0.08),
        r:   rand(0.5, 1.5),
        op:  rand(0.1, 0.3),
      });
    }
  }

  function initAsteroids() {
    asteroids = [];
    for (let i = 0; i < ASTEROID_COUNT; i++) {
      const orbitIndex = randInt(0, orbits.length - 1);
      const direction  = Math.random() < 0.5 ? 1 : -1;
      asteroids.push({
        orbitIndex,
        angle: rand(0, Math.PI * 2),
        speed: direction * rand(0.002, 0.007),
        size:  rand(1.5, 5),
        opacity: rand(0.3, 1.0),
        trail:   [],
      });
    }
  }

  // ─────────────────────────────────────────────
  // Draw helpers
  // ─────────────────────────────────────────────

  function drawStars(timestamp) {
    for (const s of stars) {
      const flicker = Math.sin(timestamp * s.speed + s.phase);
      const op = s.baseOp + flicker * 0.08;
      ctx.beginPath();
      ctx.arc(s.x, s.y, s.r, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(255,255,255,${Math.max(0, op)})`;
      ctx.fill();
    }
  }

  function updateAndDrawFloatingParticles() {
    for (const p of floatingParticles) {
      p.x += p.vx;
      p.y += p.vy;

      // Wrap around
      if (p.x < 0)       p.x = width;
      if (p.x > width)   p.x = 0;
      if (p.y < 0)       p.y = height;
      if (p.y > height)  p.y = 0;

      ctx.beginPath();
      ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(255,255,255,${p.op})`;
      ctx.fill();
    }
  }

  function drawOrbits() {
    for (const orbit of orbits) {
      ctx.save();
      ctx.translate(cx, cy);
      ctx.rotate(orbit.tilt);
      ctx.scale(1, orbit.scaleY);

      ctx.beginPath();
      ctx.ellipse(0, 0, orbit.radiusX, orbit.radiusX, 0, 0, Math.PI * 2);
      ctx.strokeStyle = 'rgba(255,255,255,0.08)';
      ctx.lineWidth   = 0.5;
      ctx.stroke();

      ctx.restore();
    }
  }

  function drawEarth() {
    const r   = earthRadius();
    const atm = r * 1.15;

    // ── Atmosphere glow ──
    const atmGrad = ctx.createRadialGradient(cx, cy, r * 0.9, cx, cy, atm);
    atmGrad.addColorStop(0, 'rgba(200,200,200,0.08)');
    atmGrad.addColorStop(1, 'transparent');

    ctx.beginPath();
    ctx.arc(cx, cy, atm, 0, Math.PI * 2);
    ctx.fillStyle = atmGrad;
    ctx.fill();

    // ── Globe gradient (light from upper-right) ──
    const lightOffX = cx + r * 0.35;
    const lightOffY = cy - r * 0.35;
    const globe = ctx.createRadialGradient(lightOffX, lightOffY, r * 0.05, cx, cy, r);
    globe.addColorStop(0,   '#8a8a8a');
    globe.addColorStop(0.5, '#4a4a4a');
    globe.addColorStop(1,   '#1a1a1a');

    ctx.beginPath();
    ctx.arc(cx, cy, r, 0, Math.PI * 2);
    ctx.fillStyle = globe;
    ctx.fill();

    // ── Abstract continents ──
    ctx.save();
    ctx.beginPath();
    ctx.arc(cx, cy, r, 0, Math.PI * 2);
    ctx.clip(); // keep continents inside the globe

    ctx.fillStyle = 'rgba(50,50,50,0.6)';

    // Continent A — upper-left blob
    const p1 = new Path2D();
    p1.moveTo(cx - r * 0.5, cy - r * 0.3);
    p1.bezierCurveTo(
      cx - r * 0.6, cy - r * 0.6,
      cx - r * 0.1, cy - r * 0.65,
      cx + r * 0.05, cy - r * 0.4
    );
    p1.bezierCurveTo(
      cx + r * 0.15, cy - r * 0.2,
      cx - r * 0.2, cy - r * 0.1,
      cx - r * 0.5, cy - r * 0.3
    );
    ctx.fill(p1);

    // Continent B — right blob
    const p2 = new Path2D();
    p2.moveTo(cx + r * 0.3, cy - r * 0.1);
    p2.bezierCurveTo(
      cx + r * 0.6, cy - r * 0.25,
      cx + r * 0.75, cy + r * 0.15,
      cx + r * 0.5, cy + r * 0.35
    );
    p2.bezierCurveTo(
      cx + r * 0.25, cy + r * 0.5,
      cx + r * 0.1,  cy + r * 0.25,
      cx + r * 0.3,  cy - r * 0.1
    );
    ctx.fill(p2);

    // Continent C — lower-left blob
    const p3 = new Path2D();
    p3.moveTo(cx - r * 0.4, cy + r * 0.2);
    p3.bezierCurveTo(
      cx - r * 0.55, cy + r * 0.1,
      cx - r * 0.65, cy + r * 0.45,
      cx - r * 0.35, cy + r * 0.6
    );
    p3.bezierCurveTo(
      cx - r * 0.1,  cy + r * 0.7,
      cx - r * 0.05, cy + r * 0.45,
      cx - r * 0.4,  cy + r * 0.2
    );
    ctx.fill(p3);

    ctx.restore();

    // ── Specular highlight ──
    const hx = cx + r * 0.28;
    const hy = cy - r * 0.28;
    const highlight = ctx.createRadialGradient(hx, hy, 0, hx, hy, r * 0.4);
    highlight.addColorStop(0,   'rgba(255,255,255,0.18)');
    highlight.addColorStop(0.5, 'rgba(255,255,255,0.05)');
    highlight.addColorStop(1,   'transparent');

    ctx.save();
    ctx.beginPath();
    ctx.arc(cx, cy, r, 0, Math.PI * 2);
    ctx.clip();
    ctx.beginPath();
    ctx.arc(hx, hy, r * 0.4, 0, Math.PI * 2);
    ctx.fillStyle = highlight;
    ctx.fill();
    ctx.restore();

    // ── Globe border / terminator ──
    ctx.beginPath();
    ctx.arc(cx, cy, r, 0, Math.PI * 2);
    ctx.strokeStyle = 'rgba(255,255,255,0.06)';
    ctx.lineWidth   = 1;
    ctx.stroke();
  }

  /**
   * Draw all asteroids, split into back and front layers.
   * @param {'back'|'front'} layer
   */
  function drawAsteroids(layer) {
    for (const ast of asteroids) {
      const orbit = orbits[ast.orbitIndex];
      const front = isInFront(orbit, ast.angle);
      if (layer === 'back'  &&  front) continue;
      if (layer === 'front' && !front) continue;

      const pos = orbitToCanvas(orbit, ast.angle);

      // ── Trail ──
      if (ast.trail.length > 1) {
        for (let i = 1; i < ast.trail.length; i++) {
          const t  = i / ast.trail.length;
          const op = t * ast.opacity * 0.6;
          const tp = ast.trail[i - 1];
          const tc = ast.trail[i];
          ctx.beginPath();
          ctx.moveTo(tp.x, tp.y);
          ctx.lineTo(tc.x, tc.y);
          ctx.strokeStyle = `rgba(255,255,255,${op})`;
          ctx.lineWidth   = ast.size * t * 0.6;
          ctx.lineCap     = 'round';
          ctx.stroke();
        }
      }

      // ── Asteroid body ──
      const brightness = Math.round(180 + ast.opacity * 75);
      ctx.beginPath();
      ctx.arc(pos.x, pos.y, ast.size, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(${brightness},${brightness},${brightness},${ast.opacity})`;
      ctx.fill();

      // Subtle specular dot on asteroid
      if (ast.size > 2.5) {
        ctx.beginPath();
        ctx.arc(pos.x - ast.size * 0.25, pos.y - ast.size * 0.25, ast.size * 0.3, 0, Math.PI * 2);
        ctx.fillStyle = `rgba(255,255,255,${ast.opacity * 0.5})`;
        ctx.fill();
      }
    }
  }

  function updateAsteroids() {
    const TRAIL_LENGTH = 22;

    for (const ast of asteroids) {
      ast.angle += ast.speed;

      const pos = orbitToCanvas(orbits[ast.orbitIndex], ast.angle);
      ast.trail.push({ x: pos.x, y: pos.y });
      if (ast.trail.length > TRAIL_LENGTH) {
        ast.trail.shift();
      }
    }
  }

  // ─────────────────────────────────────────────
  // Animation loop
  // ─────────────────────────────────────────────

  function renderStaticFrame(timestamp) {
    // Clear with solid background
    ctx.fillStyle = '#080808';
    ctx.fillRect(0, 0, width, height);

    drawStars(timestamp || 0);
    // No floating particles in static frame (they don't move)
    for (const p of floatingParticles) {
      ctx.beginPath();
      ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(255,255,255,${p.op})`;
      ctx.fill();
    }
    drawOrbits();
    drawAsteroids('back');
    drawEarth();
    drawAsteroids('front');
  }

  function animate(timestamp) {
    const deltaTime = timestamp - lastTime; // available for future use (physics scaling)
    lastTime = timestamp;

    // Clear
    ctx.fillStyle = '#080808';
    ctx.fillRect(0, 0, width, height);

    // Layer 1 – Stars (bottom)
    drawStars(timestamp);

    // Layer 2 – Floating particles
    updateAndDrawFloatingParticles();

    // Layer 3 – Orbits
    drawOrbits();

    // Update asteroid positions (trail included)
    updateAsteroids();

    // Layer 4 – Asteroids behind Earth
    drawAsteroids('back');

    // Layer 5 – Earth
    drawEarth();

    // Layer 6 – Asteroids in front of Earth
    drawAsteroids('front');

    animFrameId = requestAnimationFrame(animate);
  }

  // ─────────────────────────────────────────────
  // Resize handling
  // ─────────────────────────────────────────────

  function handleResize() {
    computeDimensions();
    initOrbits();
    initStars();
    initFloatingParticles();
    // Re-attach existing asteroids to valid orbit indices
    for (const ast of asteroids) {
      ast.orbitIndex = Math.min(ast.orbitIndex, orbits.length - 1);
      ast.trail = [];
    }
  }

  // ─────────────────────────────────────────────
  // Public init
  // ─────────────────────────────────────────────

  function initSpaceAnimation() {
    canvas = document.getElementById('spaceCanvas');
    if (!canvas) {
      console.warn('[space-animation] #spaceCanvas not found in DOM.');
      return;
    }

    ctx = canvas.getContext('2d');
    if (!ctx) {
      console.warn('[space-animation] Unable to get 2D context.');
      return;
    }

    // Initial layout
    computeDimensions();
    initOrbits();
    initStars();
    initFloatingParticles();
    initAsteroids();

    // Responsive resize via ResizeObserver
    const resizeTarget = canvas.parentElement || document.documentElement;
    if (typeof ResizeObserver !== 'undefined') {
      const ro = new ResizeObserver(() => handleResize());
      ro.observe(resizeTarget);
    } else {
      // Fallback for older browsers
      window.addEventListener('resize', handleResize);
    }

    // Check accessibility preference
    const prefersReducedMotion =
      window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    if (prefersReducedMotion) {
      // Render a single static frame and stop
      renderStaticFrame(0);
      return;
    }

    // Start animation loop
    lastTime = 0;
    animFrameId = requestAnimationFrame(animate);
  }

  // ─────────────────────────────────────────────
  // Auto-start on DOMContentLoaded or immediately
  // if the document is already ready
  // ─────────────────────────────────────────────
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initSpaceAnimation);
  } else {
    initSpaceAnimation();
  }

  // Expose for external use if needed
  window.initSpaceAnimation = initSpaceAnimation;
})();
