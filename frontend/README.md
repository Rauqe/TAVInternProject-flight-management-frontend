# Flight Management System

## Test Stratejisi

- **Unit Test:** Vue bileşenleri için Vitest ile birim testler yazılmıştır. Örnek: `src/pages/FlightCreate.spec.js`
- **E2E Test:** Uygulamanın uçtan uca işleyişi Cypress ile test edilir. Örnek: `cypress/e2e/flight_create.cy.js`
- **Erişilebilirlik Testi:** Cypress + cypress-axe ile otomatik erişilebilirlik testi yapılır. Örnek: `cypress/e2e/a11y.cy.js`
- **Coverage:** Kod kapsamı için `npx vitest run --coverage` komutu ile coverage raporu alınabilir.
- **Çalıştırmak için:**
  - Unit test: `npx vitest`
  - E2E test: `npx cypress open` veya `npx cypress run`
  - Coverage: `npx vitest run --coverage`

Testler, form doğrulama, kullanıcı akışı, erişilebilirlik ve hata durumlarını kapsamaktadır.

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
#   T A V - I n t e r n - P r o j e c t - F u l l s t a c k  
 