package pt.ipp.isep.dei.controller;

import java.util.Objects;
import pt.ipp.isep.dei.repository.AuthenticationRepository;
import pt.ipp.isep.dei.user.User;

/**
 * Controller for user registration and login flows.
 */
public class LoginController {

    private final AuthenticationRepository repo = new AuthenticationRepository();

    /**
     * Regista um novo utilizador com username e password.
     *
     * @param username o username escolhido; não pode ser nulo ou vazio
     * @param password a password escolhida; deve satisfazer a política
     * @return o User criado
     * @throws NullPointerException     se username ou password forem nulos
     * @throws IllegalArgumentException se username for vazio ou password inválida
     */
    public User register(String username, String password) {
        Objects.requireNonNull(username, "Username must not be null");
        Objects.requireNonNull(password, "Password must not be null");
        return repo.registerUser(username, password);
    }

    /**
     * Tenta autenticar um utilizador existente.
     *
     * @param id       o ID retornado no registo
     * @param password a password fornecida
     * @return o User autenticado, ou null se credenciais inválidas
     */
    public User login(String id, String password) {
        if (id == null || password == null) {
            return null;
        }
        return repo.authenticate(id, password);
    }
}
