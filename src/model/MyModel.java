package model;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.IO.MyCompressorOutputStream;
import algorithms.IO.MyDecompressorInputStream;
import algorithms.demo.Maze3DSearchable;
import algorithms.demo.MazeSearcherFactory;
import algorithms.mazeGenerators.ChooseRandomNode;
import algorithms.mazeGenerators.GrowingTreeGenerator3D;
import algorithms.mazeGenerators.Maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import presenter.Presenter;
import presenter.Properties;
import presenter.PropertiesLoader;
import presenter.PropertiesSaver;
import view.View;

/**
 * MyModel
 * <p>Implements all of Model's functions and handling all the program's backend tasks and IO using multi threading.
 * <p>Created by Ohad on 15/09/2016.
 * @author Ohad
 * @version 1.0
 * @see View
 * @see Controller
 */
public class MyModel extends Observable implements Model {
    Presenter presenter;
    private Map<String, Maze3DSearchable<Position>> mazes;
    private Map<String, Solution<Position>> solutions;
    private Properties properties;
    
	private ExecutorService executor;

    public MyModel() {
    	PropertiesSaver.getInstance();
		properties = PropertiesLoader.getInstance().getProperties();
		executor = Executors.newFixedThreadPool(properties.getNumOfThreads());
		loadMazesAndSolutions(properties.getMazeSolutionsFileName());
		
        this.mazes = new ConcurrentHashMap<>();
        this.solutions = new HashMap<>();
    }


	@Override
    public void generateMaze(String name, int cols, int rows, int layers) {
		executor.submit(new Callable<Maze3D>() {
			@Override
			public Maze3D call() throws Exception {
				GrowingTreeGenerator3D generator = new GrowingTreeGenerator3D(new ChooseRandomNode());
	            Maze3D maze = generator.generate(cols, rows, layers);
	            mazes.put(name, new Maze3DSearchable<Position>(maze));
				setChanged();
				notifyObservers("maze_ready " + name);		
				return maze;
			}
		});
    }
    
    @Override
    public Solution<Position> solveMaze(String mazeName, String strategy) {
    	try {
    		if (solutions.get(mazeName) != null) return solutions.get(mazeName);
			Future<Solution<Position>> solution = executor.submit(new Callable<Solution<Position>>() {
				@Override
				public Solution<Position> call() throws Exception {
			        Maze3DSearchable<Position> maze = mazes.get(mazeName);
			        Searcher<Position> searcher;
		            MazeSearcherFactory msf = new MazeSearcherFactory();
		            searcher = msf.getSearcher(strategy);
	
		            Solution<Position> sol = searcher.search(maze);
		            setChanged();
		            notifyObservers("solution_ready " + mazeName);
		            return sol;
				}
			});
			return solution.get();
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	return null;
    }

    @Override
    public Maze3D getMaze(String name) throws NullPointerException{
		return mazes.get(name).getMaze();
    }

    @Override
    public void exit() throws InterruptedException{
		PropertiesSaver.saveProperties(this.properties);
        saveMazesAndSolutions(this.properties.getMazeSolutionsFileName());
        
        this.executor.shutdown();
        this.executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void saveMaze(String mazeName, String fileName) {
        SaveMazeRunnable saveMazeRunnable = new SaveMazeRunnable(mazeName, fileName);
        executor.execute(saveMazeRunnable);
    }
    @Override
    public void saveMazesAndSolutions(String fileName) {
		SaveMazeSolutionsRunnable saveMazeSolutionsRunnable = new SaveMazeSolutionsRunnable(fileName);
        executor.execute(saveMazeSolutionsRunnable);
    }
    @Override
    public void loadMaze(String name, String fileName) {
        LoadMazeRunnable loadMazeRunnable = new LoadMazeRunnable(name, fileName);
        executor.execute(loadMazeRunnable);
    }
	public void loadMazesAndSolutions(String fileName) {
		LoadMazeSolutionsRunnable loadMazeSolutionsRunnable = new LoadMazeSolutionsRunnable(fileName);
        executor.execute(loadMazeSolutionsRunnable);
	}
    
    
    public int [][] getCrossSection(String name, String section, int index) {
        int [][] arr;
        section = section.toUpperCase();
        switch (section) {
            case "X": arr = mazes.get(name).getMaze().getCrossSectionByX(index);
                break;
            case "Y": arr = mazes.get(name).getMaze().getCrossSectionByY(index);
                break;
            case "Z": arr = mazes.get(name).getMaze().getCrossSectionByZ(index);
                break;
            default:
                arr = new int[0][0];
        }
        return arr;
    }
    
    private static void close(Closeable c) {
		if (c == null) return;
		try {
		    c.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
    
    @Override
    public void setSolution(String name, Solution<Position> solution) {
        solutions.put(name, solution);
    }
    
    @Override
    public Solution<Position> getSolution(String name) {
    	return solutions.get(name);
    }
    
    class SaveMazeRunnable implements Runnable {

        private String mazeName;
        private String fileName;
        MyCompressorOutputStream out = null;

        public SaveMazeRunnable(String mazeName, String fileName) {
            this.mazeName = mazeName;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            if (mazes.containsKey(mazeName)) {
                try {
                    out = new MyCompressorOutputStream(new FileOutputStream(fileName));
                    byte[] arr = mazes.get(mazeName).getMaze().toByteArray();
                    out.write(arr.length / 255);
                    out.write(arr.length % 255);
                    out.write(arr);
    	            setChanged();
    	            notifyObservers("maze_saved " + mazeName + " " + fileName);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    close(out);
                }
            } else {
	            setChanged();
	            notifyObservers("maze_not_found " + mazeName);
            }
        }

        public void terminate() {
            out.setDone(true);
        }
    }
    class SaveMazeSolutionsRunnable implements Runnable {

        private String fileName;
        GZIPOutputStream out = null;

        public SaveMazeSolutionsRunnable(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void run() {
        	ObjectOutputStream oos = null;
    		try {
    		    oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("solutions.dat")));
    			oos.writeObject(mazes);
    			oos.writeObject(solutions);
                setChanged();
                notifyObservers("mazes_solutions_saved " + fileName);	
    		} catch (FileNotFoundException e) {
                setChanged();
                notifyObservers("mazes_solutions_save_failed " + fileName);
    			e.printStackTrace();
    		} catch (IOException e) {
                setChanged();
                notifyObservers("mazes_solutions_save_failed " + fileName);
    			e.printStackTrace();
    		} finally {
    			try {
    				oos.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
        }
    }
    class LoadMazeRunnable implements Runnable {

        private String mazeName;
        private String fileName;
        MyDecompressorInputStream in = null;

        public LoadMazeRunnable(String mazeName, String fileName) {
            this.mazeName = mazeName;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            if (!mazes.containsKey(mazeName)) {
                try {
                    in = new MyDecompressorInputStream(new FileInputStream(fileName));
                    int size = in.read() * 255;
                    size += in.read();
                    byte[] mazeInBytes = new byte[size];
                    in.read(mazeInBytes);
                    mazes.put(mazeName, new Maze3DSearchable<Position>(new Maze3D(mazeInBytes)));
    	            setChanged();
    	            notifyObservers("maze_loaded " + mazeName);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    close(in);
                }
            } else{
	            setChanged();
	            notifyObservers("maze_already_exists " + mazeName);
            }
        }

        public void terminate() {
            in.setDone(true);
        }
    }
    class LoadMazeSolutionsRunnable implements Runnable {
        File file;
        GZIPOutputStream out = null;

        public LoadMazeSolutionsRunnable(String fileName) {
            file = new File(fileName);
        }

        @SuppressWarnings("unchecked")
		@Override
        public void run() {
    		if (!file.exists()) {
                setChanged();
                notifyObservers("mazes_solutions_load_failed " + file.getName());
    		}
    		ObjectInputStream ois = null;
    		try {
    			ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream("solutions.dat")));
    			mazes = (Map<String, Maze3DSearchable<Position>>)ois.readObject();
    			solutions = (Map<String, Solution<Position>>)ois.readObject();
                setChanged();
                notifyObservers("mazes_solutions_loaded");	
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
                setChanged();
                notifyObservers("mazes_solutions_load_failed " + file.getName());
    		} catch (IOException e) {
    			e.printStackTrace();
                setChanged();
                notifyObservers("mazes_solutions_load_failed " + file.getName());
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
                setChanged();
                notifyObservers("mazes_solutions_load_failed " + file.getName());
    		} finally{
    			try {
    				ois.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
        }
    }
}