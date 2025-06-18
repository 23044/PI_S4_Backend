
/**
 * Dashboard functionality
 * Handles navigation, content loading, and dashboard specific features
 */

document.addEventListener('DOMContentLoaded', function() {
  checkAuthentication();
  initDashboardNav();
  loadDashboardData();
  initSendMessageForm();
  loadConversations();
});

function checkAuthentication() {
  // Implémentation à ajouter selon votre logique d'authentification
}

function initDashboardNav() {
  const navLinks = document.querySelectorAll('.sidebar-nav a');
  const sections = document.querySelectorAll('.dashboard-section');
  const pageTitle = document.querySelector('.page-title');

  navLinks.forEach(link => {
    link.addEventListener('click', function(e) {
      e.preventDefault();
      const targetSection = this.getAttribute('data-section');
      navLinks.forEach(link => link.classList.remove('active'));
      this.classList.add('active');
      sections.forEach(section => {
        if (section.id === targetSection) {
          section.classList.add('active');
          if (pageTitle) pageTitle.textContent = this.textContent.trim();
        } else {
          section.classList.remove('active');
        }
      });
      const sidebar = document.querySelector('.sidebar');
      if (window.innerWidth < 992 && sidebar) sidebar.classList.remove('active');
    });
  });

  const cardActions = document.querySelectorAll('.card-action[data-section]');
  cardActions.forEach(action => {
    action.addEventListener('click', function(e) {
      e.preventDefault();
      const targetSection = this.getAttribute('data-section');
      const targetLink = document.querySelector(`.sidebar-nav a[data-section="${targetSection}"]`);
      if (targetLink) targetLink.click();
    });
  });
}

function loadDashboardData() {
  const user = getData('doctolearn_user');
  if (user) {
    const thesisTitle = document.getElementById('thesis-title');
    if (thesisTitle) thesisTitle.textContent = user.thesisTitle;
  }
}

function initSendMessageForm() {
  const form = document.getElementById("sendMessageForm");
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const to = document.getElementById("receiverEmail").value.trim();
    const content = document.getElementById("messageContent").value.trim();
    const file = document.getElementById("fileInput").files[0];

    if (!to || (!content && !file)) {
      showToast("Veuillez remplir tous les champs ou sélectionner un fichier", "error");
      return;
    }

    try {
      if (file) {
        const formData = new FormData();
        formData.append("to", to);
        formData.append("file", file);
        await fetch("http://localhost:8081/api/messages/send-file", {
          method: "POST",
          credentials: "include",
          body: formData
        });
      } else {
        await fetch("http://localhost:8081/api/messages/send", {
          method: "POST",
          credentials: "include",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ to, content })
        });
      }
      form.reset();
      loadConversation(to);
    } catch (err) {
      console.error("Erreur envoi message", err);
      showToast("Erreur lors de l'envoi du message", "error");
    }
  });
}

let selectedContact = null;

async function loadConversations() {
  try {
    const response = await fetch("http://localhost:8081/api/messages/conversations", { credentials: "include" });
    const list = await response.json();
    const ul = document.getElementById("conversationList");
    ul.innerHTML = "";

    list.forEach(c => {
      const li = document.createElement("li");
      li.innerHTML = `<strong>${c.contactEmail}</strong><br><small>${c.lastMessage}</small>`;
      li.addEventListener("click", () => {
        selectedContact = c.contactEmail;
        document.getElementById("receiverEmail").value = selectedContact;
        document.getElementById("chatHeader").innerHTML = `<h3>Conversation avec ${selectedContact}</h3>`;
        loadConversation(selectedContact);
      });
      ul.appendChild(li);
    });
  } catch (err) {
    console.error("Erreur lors du chargement des conversations", err);
    showToast("Impossible de charger les conversations", "error");
  }
}

async function loadConversation(withEmail) {
  try {
    const response = await fetch(`http://localhost:8081/api/messages/conversation?with=${encodeURIComponent(withEmail)}`, {
      credentials: "include"
    });
    const messages = await response.json();
    const chatWindow = document.getElementById("chatWindow");
    chatWindow.innerHTML = "";

    messages.forEach(msg => {
      const div = document.createElement("div");
      div.style.textAlign = msg.from === selectedContact ? "left" : "right";
      div.innerHTML = `
        <div style="display:inline-block; padding: 5px; border-radius: 5px; background-color: #eee; margin: 5px;">
          ${msg.content.includes("uploads/") ? `<img src="${msg.content}" style="max-width:100px;">` : msg.content}
          <br><small>${new Date(msg.timestamp).toLocaleString()}</small>
        </div>
      `;
      chatWindow.appendChild(div);
    });

    chatWindow.scrollTop = chatWindow.scrollHeight;
  } catch (err) {
    console.error("Erreur lors du chargement des messages", err);
    showToast("Impossible de charger la conversation", "error");
  }
}
