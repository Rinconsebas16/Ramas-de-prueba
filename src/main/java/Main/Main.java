package Main;

import Application.services.DarAcceso.LoginService;
import Application.services.DarAcceso.RegistroService;
import Application.services.ListarCursosService;
import Application.services.PomodoroTimer;
import Application.services.SesionPomodoroService;

import Domain.repositoriesInterfaces.*;
import Infrastructure.controllers.ControllerControladores;
import Infrastructure.persistence.ConexionBD;
import Infrastructure.persistence.H2DataBaseInitializer;
import Infrastructure.repositories.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // 1️⃣ Inicializar conexión con la base de datos
        var connMgr = new ConexionBD();
        var initializer = new H2DataBaseInitializer(connMgr);
        initializer.initialize();

        // 2️⃣ Instanciar repositorios existentes
        InterfazUsuarioRepository usuarioRepository = new UsuarioRepository(connMgr);
        InterfazCursoRepository cursoRepository = new CursoRepository(connMgr);
        InterfazUsuarioCursoRepository usuarioCursoRepository = new UsuarioCursoRepository(connMgr);
        InterfazSesionEstudioRepository sesionEstudioRepository = new SesionEstudioRepository(connMgr);
        InterfazUsuarioStatsRepository usuarioStatsRepository = new UsuarioStatsRepository(connMgr);
        // SeccionRepository existe, pero no se usa aún directamente

        // 3️⃣ Instanciar servicios de aplicación válidos
        ListarCursosService listarCursosService = new ListarCursosService(
                cursoRepository, usuarioCursoRepository
        );
        PomodoroTimer pomodoroTimer = PomodoroTimer.getInstance();
        SesionPomodoroService sesionPomodoroService = new SesionPomodoroService(sesionEstudioRepository);
        LoginService loginService = new LoginService(usuarioRepository);
        RegistroService registroService = new RegistroService(usuarioRepository);

        // 4️⃣ Crear el orquestador (front controller)
        ControllerControladores controllerControladores = new ControllerControladores(
                listarCursosService,
                pomodoroTimer,
                sesionPomodoroService,
                loginService,
                registroService
        );

        // 5️⃣ Mostrar la primera vista desde el orquestador
        controllerControladores.mostrarVistaInicial(stage);

        // 6️⃣ Configuración visual del Stage
        stage.setTitle("STELLA - Inicio");
        stage.setResizable(false);
        stage.setWidth(1920);
        stage.setHeight(1080);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


