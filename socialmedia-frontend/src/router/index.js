import { createRouter, createWebHistory } from 'vue-router';
import Dashboard from '@/views/Dashboard.vue';
import Login     from '@/views/Login.vue';
import Signup    from '@/views/Signup.vue';
import TwoFactor from '@/views/TwoFactor.vue';
import Setup2FA  from '@/views/Setup2FA.vue';
import PostDetail from "@/views/PostDetail.vue";

const routes = [
    {
        path: '/',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true }
    },
    {
        path: '/signup',
        name: 'Signup',
        component: Signup
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/2fa',
        name: 'TwoFactor',
        component: TwoFactor
    },
    {
        path: '/2fa/setup',
        name: 'Setup2FA',
        component: Setup2FA,
        meta: { requiresAuth: true }
    },
    {
        path: '/posts/:id',
        name: 'PostDetail',
        component: PostDetail,
        props: true
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

// simple auth guard
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem("jwt");
    const publicPages = ["Login", "Signup", "TwoFactor"];
    if (publicPages.includes(to.name)) return next();
    if (to.name === "Setup2FA" && !token) return next({ name: "Login" });
    if (!token) return next({ name: "Login" });
    next();
});

export default router;
