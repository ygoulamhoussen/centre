<template>
  <div class="login-page">
    <h1>Connexion</h1>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" v-model="email" required />
      </div>
      <div class="form-group">
        <label for="password">Mot de passe</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit">Se connecter</button>
      <button type="button" @click="cancel">Annuler</button> <!-- Add cancel button -->
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      <p v-if="successMessage" class="success">{{ successMessage }}</p> <!-- Add success message -->
    </form>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      email: "",
      password: "",
      errorMessage: "",
      successMessage: "" // Add success message data property
    };
  },
  methods: {
    async login() {
      console.log('Login method called'); // Debugging line
      console.log('Email:', this.email); // Debugging line
      console.log('Password:', this.password); // Debugging line
      try {
        const response = await fetch('http://localhost:8080/api/utilisateurs/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ email: this.email, motDePasse: this.password })
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log('Login successful:', data); // Debugging line
        this.errorMessage = "";
        this.successMessage = "Connexion réussie !"; // Set success message
        sessionStorage.setItem('userProfile', JSON.stringify(data)); // Store user profile in session
        this.$router.push('/'); // Redirect to homepage
        // ... handle successful login ...
      } catch (error) {
        console.error('Error during login:', error);
        this.errorMessage = 'Erreur lors de la connexion. Veuillez réessayer.';
        this.successMessage = ""; // Clear success message on error
      }
    },
    cancel() {
      this.$router.push('/'); // Redirect to homepage
    }
  }
};
</script>

<style scoped>
.login-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f9f9f9;
}

.login-page h1 {
  text-align: center;
  margin-bottom: 20px;
}

form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

button[type="button"] {
  background-color: #6c757d;
}

button:hover {
  background-color: #0056b3;
}

button[type="button"]:hover {
  background-color: #5a6268;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}

.success {
  color: green;
  margin-top: 10px;
  text-align: center;
}
</style>
