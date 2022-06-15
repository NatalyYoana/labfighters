package main;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	//dimensiones de la ventana del juego
	private static final int WIDTH =800;
	private static final int LENGHT = 600;
	
	//Boolean para saber si el juego se esta ejucutando o no
	private static volatile boolean enFuncionamiento = false;
	
	//nombre de la ventana
	private static final String NOMBRE = "LAB FIGHTERS";
	private static int aps = 0;
	private static int fps = 0;
	
	
	
	private static Thread thread;
	
	//creacion de la ventana para el juego
	private static JFrame ventana;
	
	private Window() {
		
		setPreferredSize(new Dimension(WIDTH,LENGHT));
		
		ventana = new JFrame(NOMBRE);
		//Parametro para cerrar la ventana 
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//parametro para que el usuario no pueda rejustar el tamaño de la ventana
		ventana.setResizable(false);
		ventana.setLayout(new BorderLayout());
		//la ventana siempre saldra en el centro de la pantalla
		ventana.add(this, BorderLayout.CENTER);
		ventana.pack();
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
		
	}
	
	//primera instancia de la clase 
	
	public static void main(String[] args) {
		Window window = new Window();
		window.iniciar();
		
	}
	
	//Instancia para iniciar el thread
	private synchronized void iniciar() {
		enFuncionamiento = true;
		thread = new Thread(this, "Graficos");
		thread.start();
		
	}
	
	
	//Instancia para detener el thread
	private synchronized void detener() {
		enFuncionamiento = false;
		
		//con este try podemos ver los errores en consola 
		try {
			thread.join();
		} catch (InterruptedException e) {	
			e.printStackTrace();
		}
		
	}
	
	
	private void actualizar() {
		aps++;
		
		
	}
	
	private void mostrar() {
		fps++;
		
	}

	
	//lo que esta en este blucle se ejecuta mientras el juego este corriendo
	
	@Override
	public void run() {
		final int NS_POR_SEGUNDO =1000000000;
		final byte APS_OBJETIVO = 60;
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;
		
		long referenciaActualizacion = System.nanoTime();
		long referenciaContador = System.nanoTime();
		
		double tiempoTranscurrido;
		double delta = 0;
		
		
		
		while (enFuncionamiento) {
			
			final long inicioBucle = System.nanoTime();
			tiempoTranscurrido = inicioBucle - referenciaActualizacion;
			referenciaActualizacion = inicioBucle;
			
			delta += tiempoTranscurrido /NS_POR_ACTUALIZACION;
			
			while(delta >= 1) {
				actualizar();
				delta--;
				
			}
			
			//para mostrar los graficos y actualizar en todo momento cada vez que se ejecute una accion
			actualizar();
			mostrar();
			
			if( System.nanoTime()-referenciaContador > NS_POR_SEGUNDO) {
				
				ventana.setTitle(NOMBRE + " || APS: " + aps + "|| FPS: " + fps );
				aps =0;
				fps =0;
				referenciaContador = System.nanoTime();
				
			}
			
		}
		
		
		
	}
	
}