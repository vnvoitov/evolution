package com.evolution;

import com.evolution.area.Area;
import com.evolution.creature.Agent;
import com.evolution.meal.Meal;
import com.evolution.service.HelloMessageService;
import com.evolution.utily.SortBySense;
import com.evolution.utily.SpeedVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

import static java.lang.System.exit;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {

	@Autowired
	private HelloMessageService helloService;

	@Value("${numMeal}")
	private int numMeal;
	@Value("${enrgMeal}")
	private int enrgMeal;
	@Value("${numAgent}")
	private int numAgent ;
	@Value("${senseRangeMax}")
	private int senseRangeMax ;
	@Value("${acqRange}")
	private int acqRange ;
	@Value("${enrgAgent}")
	private int enrgAgent ;
	@Value("${strengthMax}")
	private int strengthMax ;

	private SpeedVector speedVector;
	@Value("${speedMax}")
	private int speedMax;  // >1

	@Value("${iterations}")
	private int iterations;

	public static void main(String[] args) throws Exception {

		//disabled banner, don't want to see the spring logo
		SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

		//SpringApplication.run(SpringBootConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		System.out.println("Started");
		Area area = new Area(1000,1000);

		List<Meal> meals = new ArrayList<>();
		for (int i=0; i<numMeal;i++) {
			meals.add(new Meal(area.getWidth(), area.getWidth(), enrgMeal));
		}

		List<Agent> agents = new ArrayList<>();
		for (int i=0; i<numAgent; i++) {
			agents.add(new Agent(area.getWidth(),
					area.getHeigh(),
					senseRangeMax,
					acqRange,
					enrgAgent,
					speedMax,
					strengthMax)
			);
		}

		int k =0 ;
		for (Agent a: agents) {
			System.out.println("Agent " + k + ": x=" + a.getX() + " y=" + a.getY() + " vector.speed=" + a.getSpeedVector().getSpeed());
			k ++ ;
		}

//        k =0 ;
//        for (Meal m: meals) {
//            System.out.println("Meal before " + k + ": x=" + m.getX() + " y=" + m.getY() + " energy=" + m.getEnergy());
//            k ++ ;
//        }
		k=0;
		for (int i=0; i<iterations; i++) {
			Collections.sort(agents);
			// Агенты едят пищу
			for (Agent a: agents) {
				Iterator m_i = meals.iterator();
				while (m_i.hasNext()) {
					Meal m = (Meal) m_i.next();
					if (a.isEatable(m)) {
//                        System.out.println("Agent energy before=" + a.getEnergy());
						a.addEnergy(m.getEnergy());
//                        System.out.println("Meal " + k++ + " Eaten. x=" + m.getX() + " y=" + m.getY());
//                        System.out.println("Agent energy after=" + a.getEnergy());
						m_i.remove();
					}
				}
			}
			// Агенты едят друг друга
			k=0;
			for (int j=0; j<agents.size();j++) {
				if (agents.get(j).getStrength() != 0) {
					for (int l = j + 1; l < agents.size(); l++) {
						if (!agents.get(l).isKilled()) {
							if (agents.get(j).isEatable(agents.get(l))) {
								System.out.println("Agent killed " + k++ + ": x=" + agents.get(l).getX() + " y=" + agents.get(l).getY() + " vector.speed=" + agents.get(l).getSpeedVector().getSpeed());
								agents.get(j).addEnergy(agents.get(l).getEnergy());
								agents.get(l).kill();
							}
						}
					}
				}
			}
			// Передвигаем
			// Начинаем с самых чувствительных
			Collections.sort(agents, new SortBySense());
			for (int j=0; j<agents.size();j++) {
				if (!agents.get(j).isKilled()) {
					float strength = 0;
					int mostStrengthIndx = 0;
					for (int l=j; l<agents.size(); l++) {
						if (!agents.get(l).isKilled()) {
							if (agents.get(j).isSeen(agents.get(l))) {
								// запомнить самого сильного кандидата
								if (strength < agents.get(l).getStrength()) {
									strength = agents.get(l).getStrength();
									mostStrengthIndx = l;
								}
							}

						}
					}
					// Убегаем от самого сильного, если есть такой, или рандомно, если никого не найдено
					if (0 != strength) {
						agents.get(j).move(agents.get(mostStrengthIndx), area);
					} else {
						agents.get(j).move();
					}
				}
			}
			k =0 ;
			for (Agent a: agents) {
				System.out.println("Agents after " + k + ": x=" + a.getX() + " y=" + a.getY() + " sense=" + a.getSense_range());
				k ++ ;
			}

//            k =0 ;
//            for (Meal m: meals) {
//                System.out.println("Meal after " + k + ": x=" + m.getX() + " y=" + m.getY() + " energy=" + m.getEnergy());
//                k ++ ;
//            }
		}

		exit(0);
	}
}