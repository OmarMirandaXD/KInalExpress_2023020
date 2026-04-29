/* =========================================
   NAVBAR – scroll effect & hamburger
   ========================================= */
const navbar  = document.getElementById('navbar');
const hamburger = document.getElementById('hamburger');
const navLinks  = document.getElementById('navLinks');

window.addEventListener('scroll', () => {
  if (window.scrollY > 50) {
    navbar.classList.add('scrolled');
  } else {
    navbar.classList.remove('scrolled');
  }
  highlightActiveSection();
});

hamburger.addEventListener('click', () => {
  hamburger.classList.toggle('open');
  navLinks.classList.toggle('open');
});

// Close menu when a link is clicked
navLinks.querySelectorAll('a').forEach(link => {
  link.addEventListener('click', () => {
    hamburger.classList.remove('open');
    navLinks.classList.remove('open');
  });
});

/* =========================================
   ACTIVE NAV LINK ON SCROLL
   ========================================= */
const sections = document.querySelectorAll('section[id]');

function highlightActiveSection() {
  const scrollY = window.scrollY;
  sections.forEach(section => {
    const sectionTop    = section.offsetTop - 100;
    const sectionHeight = section.offsetHeight;
    const sectionId     = section.getAttribute('id');
    const link          = navLinks.querySelector(`a[href="#${sectionId}"]`);
    if (link) {
      if (scrollY >= sectionTop && scrollY < sectionTop + sectionHeight) {
        navLinks.querySelectorAll('a').forEach(a => a.classList.remove('active'));
        link.classList.add('active');
      }
    }
  });
}

/* =========================================
   SCROLL REVEAL
   ========================================= */
const revealObserver = new IntersectionObserver((entries) => {
  entries.forEach((entry, i) => {
    if (entry.isIntersecting) {
      // Stagger cards
      const delay = entry.target.dataset.delay || 0;
      setTimeout(() => {
        entry.target.classList.add('visible');
      }, delay);
      revealObserver.unobserve(entry.target);
    }
  });
}, { threshold: 0.12, rootMargin: '0px 0px -40px 0px' });

document.querySelectorAll('.project-card, .about-grid, .contact-grid, .hero-text').forEach((el, i) => {
  el.classList.add('reveal');
  // Stagger project cards
  if (el.classList.contains('project-card')) {
    el.dataset.delay = (i % 3) * 80;
  }
  revealObserver.observe(el);
});

/* =========================================
   CONTACT FORM (Formspree)
   ========================================= */
const contactForm = document.getElementById('contactForm');
const formStatus  = document.getElementById('formStatus');

if (contactForm) {
  contactForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const submitBtn = contactForm.querySelector('button[type="submit"]');
    submitBtn.disabled = true;
    submitBtn.textContent = 'Enviando…';

    try {
      const data = new FormData(contactForm);
      const response = await fetch(contactForm.action, {
        method: 'POST',
        body: data,
        headers: { Accept: 'application/json' },
      });

      if (response.ok) {
        formStatus.textContent = '✅ ¡Mensaje enviado! Te responderé pronto.';
        formStatus.style.color = '#22c55e';
        contactForm.reset();
      } else {
        throw new Error('Error en el servidor');
      }
    } catch {
      formStatus.textContent = '❌ Algo salió mal. Por favor escríbeme directamente.';
      formStatus.style.color = '#ef4444';
    } finally {
      submitBtn.disabled = false;
      submitBtn.textContent = 'Enviar mensaje ✉️';
    }
  });
}

/* =========================================
   TYPED EFFECT (hero title)
   ========================================= */
const roles = [
  'Desarrollador Full Stack',
  'Estudiante de Ing. en Sistemas',
  'Desarrollador Java',
  'Desarrollador Node.js',
];

const heroTitle = document.querySelector('.hero-title');
if (heroTitle) {
  let roleIndex   = 0;
  let charIndex   = 0;
  let isDeleting  = false;
  let pauseTimer  = null;

  function type() {
    const currentRole = roles[roleIndex];
    const accent      = ' <span class="accent">&</span> ';

    if (isDeleting) {
      charIndex--;
    } else {
      charIndex++;
    }

    const visible = currentRole.substring(0, charIndex);
    heroTitle.innerHTML = visible + '<span class="cursor">|</span>';

    let speed = isDeleting ? 40 : 80;

    if (!isDeleting && charIndex === currentRole.length) {
      speed = 2000;
      isDeleting = true;
    } else if (isDeleting && charIndex === 0) {
      isDeleting  = false;
      roleIndex   = (roleIndex + 1) % roles.length;
      speed = 400;
    }

    clearTimeout(pauseTimer);
    pauseTimer = setTimeout(type, speed);
  }

  // Inject cursor style once
  const cursorStyle = document.createElement('style');
  cursorStyle.textContent = '.cursor { animation: blink 0.7s step-end infinite; } @keyframes blink { 50% { opacity: 0; } }';
  document.head.appendChild(cursorStyle);

  type();
}
