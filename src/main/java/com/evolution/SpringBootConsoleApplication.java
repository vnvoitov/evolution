package com.evolution;

import com.evolution.area.Area;
import com.evolution.creature.Agent;
import com.evolution.meal.Meal;
import com.evolution.service.HelloMessageService;
import com.evolution.utily.SortByEnergy;
import com.evolution.utily.SortBySense;
import com.evolution.utily.SortBySpeed;
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
	@Value("${Cap}")
	private int Cap;
	@Value("${strengthMax}")
	private int strengthMax ;
	@Value("${criticalStrength}")
	private float criticalStrength ;
	@Value("${mutationFreq}")
	private float mutationFreq ;
	@Value("${speedMax}")
	private int speedMax;  // >1
	@Value("${mutationDepth}")
	private float mutationDepth ;

	@Value("${iterations}")
	private int iterations;

	private int agentNum = 0;
	public static void main(String[] args) throws Exception {

		//disabled banner, don't want to see the spring logo
		SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

		//SpringApplication.run(SpringBootConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Area area = new Area(1000,1000);

		List<Meal> meals = new ArrayList<>();

		List<Agent> agents = new ArrayList<>();
		for (int i=0; i<numAgent; i++) {
			agents.add(new Agent(area.getWidth(),
					area.getHeigh(),
					senseRangeMax,
					acqRange,
					Cap,
					speedMax,
					strengthMax,
					criticalStrength,
					mutationDepth,
					agentNum++)
			);
		}

		int k =0 ;
//		for (Agent a: agents) {
//			System.out.println("Agent generated " + k + ": x=" + a.getX() + " y=" + a.getY() + " energy=" + a.getEnergy());
//			k ++ ;
//		}

//        k =0 ;
//        for (Meal m: meals) {
//            System.out.println("Meal before " + k + ": x=" + m.getX() + " y=" + m.getY() + " energy=" + m.getEnergy());
//            k ++ ;
//        }
		k=0;
		for (int i=0; i<iterations; i++) {
			// Накидаем еды
			for (int j=0; j<numMeal;j++) {
				meals.add(new Meal(area.getWidth(), area.getWidth(), enrgMeal));
			}
			Collections.sort(agents, new SortBySpeed());
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
//								System.out.println("Agent " + agents.get(l).getNum() + " killed x=" + agents.get(l).getX() + " y=" + agents.get(l).getY());
								agents.get(j).addEnergy(agents.get(l).getEnergy());
								agents.get(l).kill();
								k++;
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
					// Убегаем от самого сильного, если есть такой, или движемся к еде, если никого не найдено
					if (0 != strength) {
						agents.get(j).moveFrom(agents.get(mostStrengthIndx), area);
					} else {
						// Поиск самой питательной еды
						int nearestM = 0 ;
						int nearestA = 0 ;
						int nearestIndxM = -1;
						int nearestIndxA = -1;
						for (int l=0; l<meals.size();l++) {
							if (agents.get(j).isSeen(meals.get(l))) {
								int d = agents.get(j).distance(meals.get(l));
								if (d > nearestM) {
									nearestM = d ;
									nearestIndxM = l ;
								}
							}
						}
						for (int l=0; l<agents.size(); l++) {
							if (!agents.get(l).isKilled()) {
								if (agents.get(j).isSeen(agents.get(l))) {
									if (agents.get(j).getStrength()/agents.get(l).getStrength() > criticalStrength) {
										if (agents.get(l).getEnergy() > meals.get(nearestM).getEnergy()) {
											int d = agents.get(l).getEnergy();
											if (d > nearestA) {
												nearestA = d ;
												nearestIndxA = l ;
											}
										}
									}
								}
							}
						}
						if (nearestIndxM != -1) {
							if (nearestIndxA != -1) {
								if (agents.get(nearestIndxA).getEnergy() > meals.get(nearestIndxM).getEnergy()) {
									agents.get(j).moveTo(agents.get(nearestIndxA));
								} else {
									agents.get(j).moveTo(meals.get(nearestIndxM));
								}
							} else {
								agents.get(j).moveTo(meals.get(nearestIndxM));
							}
						} else {
							// Если не найдено ни еды, ни агента, то рандомное движение
							agents.get(j).move(area);
						}
					}
				}
			}
			k =0 ;
			for (Agent a: agents) {
				if (!a.isKilled()) {
					//System.out.println("Agents after " + k + ": x=" + a.getX() + " y=" + a.getY() + " energy=" + a.getEnergy() + " senseRange=" + a.getSense_range() + " strength=" + a.getStrength() + " speed=" + a.getSpeed());
					k ++ ;
				}
			}
			// Мутации
			Collections.sort(agents, new SortByEnergy());
			int kk = Math.round(k*mutationFreq);
			System.out.println("Iteration " + i + ", Overall: " + k + ", Mutation share: " + kk);
			System.out.println("AgentNum;X;Y;Cap;ExitCost;Monetization;TimeSunken");
			for (int l=0; l<kk; l++) {
				agents.get(l).evaluate();
//				System.out.println("Agents sorted " + l + ": x=" + agents.get(l).getX() + " y=" + agents.get(l).getY() + " energy=" + agents.get(l).getEnergy() + " senseRange=" + agents.get(l).getSense_range() + " strength=" + agents.get(l).getStrength() + " speed=" + agents.get(l).getSpeed());
			}

			k =0 ;
			for (Agent a: agents) {
				if (!a.isKilled()) {
					System.out.println(a.getNum() + ";" + a.getX() + ";" + a.getY() + ";" + a.getEnergy() + ";" + a.getSense_range() + ";" + a.getStrength() + ";" + a.getSpeed());
					k ++ ;
				}
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