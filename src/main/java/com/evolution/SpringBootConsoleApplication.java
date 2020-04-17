package com.evolution;

import com.evolution.area.Area;
import com.evolution.creature.Agent;
import com.evolution.entities.Ext;
import com.evolution.genom.Genome;
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

	@Value("${areaW}")
	private int areaW;
	@Value("${areaH}")
	private int areaH;
	@Value("${numMeal}")
	private int numMeal;
	@Value("${enrgMeal}")
	private int enrgMeal;
	@Value("${numAgent}")
	private int numAgent ;
	@Value("${ExitCostMax}")
	private int ExitCostMax;
	@Value("${acqRange}")
	private int acqRange ;
	@Value("${Cap}")
	private int Cap;
	@Value("${MonetizationMax}")
	private int MonetizationMax;
	@Value("${criticalMon}")
	private float criticalMon;
	@Value("${mutationFreq}")
	private float mutationFreq ;
	@Value("${TimeSunkenMax}")
	private int TimeSunkenMax;  // >1
	@Value("${mutationDepth}")
	private float mutationDepth ;
	@Value("${iterations}")
	private int iterations;
	@Value("${criticalA}")
	private float criticalA;

	private int agentNum = 0;
	private int agentKilled=0;


	//final static Logger logger = LoggerFactory.getLogger(SpringBootConsoleApplication.class);
	public static void main(String[] args)  {

		//disabled banner, don't want to see the spring logo
		SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

		//SpringApplication.run(SpringBootConsoleApplication.class, args);
	}


	@Override
	public void run(String... args)  {
		Area area = new Area(areaW,areaH);
		//logger.info("Entering application.");
		List<Meal> meals = new ArrayList<>();
		Ext ext = new Ext();
		List<Agent> agents = new ArrayList<>();
		for (int i=0; i<numAgent; i++) {
			agents.add(new Agent(area.getWidth(),
					area.getHeigh(),
					ExitCostMax,
					acqRange,
					Cap,
					TimeSunkenMax,
					MonetizationMax,
					criticalMon,
					mutationDepth,
					agentNum++,
					ext)
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
		System.out.println("Iteration;AgentNum;X;Y;Cap;Size;ExitCost;Monetization;TimeSunken");
		k=0;
		for (int i=0; (i<iterations)&&(agentNum>agentKilled); i++) {
			ext.extChange();
//			k =0 ;
//			for (Agent a: agents) {
//				if (a.isKilled()) {
//					k++;
//				}
//			}
//			System.out.println("Agent killed " + k);
//			if (k>=1995) {
//				System.out.println("killed " + k);
//			}
			// Накидаем еды
			for (int j=0; j<numMeal;j++) {
				meals.add(new Meal(area.getWidth(), area.getWidth(), enrgMeal, ext));
			}
//			System.out.println("Iteration " + i + ". Meals: " + meals.size());
			Collections.sort(agents, new SortBySpeed());
			// Агенты едят пищу
			for (Agent a: agents) {
				Iterator m_i = meals.iterator();
				while (m_i.hasNext()) {
					Meal m = (Meal) m_i.next();
					if (!a.isKilled()) {
						if (a.isEatable(m)) {
//                        System.out.println("Agent energy before=" + a.getEnergy());
							a.addEnergy(m.getEnergy());
//                        System.out.println("Meal " + k++ + " Eaten. x=" + m.getX() + " y=" + m.getY());
//                        System.out.println("Agent energy after=" + a.getEnergy());
							m_i.remove();
						}
					}
				}
			}
			// Агенты едят друг друга
			k=0;
			for (int j=0; j<agents.size();j++) {
				if (!agents.get(j).isKilled()) {
					for (int l = j + 1; l < agents.size(); l++) {
						if (!agents.get(l).isKilled()) {
							if (agents.get(j).isEatable(agents.get(l))) {
//								System.out.println("Agent " + agents.get(l).getNum() + " killed x=" + agents.get(l).getX() + " y=" + agents.get(l).getY());
								agents.get(j).addEnergy(agents.get(l).getEnergy());
								agents.get(l).kill();
								agentKilled++;
								System.out.println("Killed: "+agentKilled + " N:" + agents.get(l).getNum());
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
					for (int l=j+1; l<agents.size(); l++) {
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
						float nearestA = 0 ;
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
						// поиск самого питательного агента
						for (int l=j+1; l<agents.size(); l++) {
							if (!agents.get(l).isKilled()) {
								if (agents.get(j).isSeen(agents.get(l))) {
									if (agents.get(j).getStrength()/agents.get(l).getStrength() > criticalMon) {
										if (agents.get(l).valueEnergy() > meals.get(nearestM).valueEnergy()) {
											float d = agents.get(l).valueEnergy();
											if (d > nearestA) {
												nearestA = d ;
												nearestIndxA = l ;
											}
										}
									}
								}
							}
						}
						// Выбор, кого съесть из найденных
						if (nearestIndxM != -1) {
							if (nearestIndxA != -1) {
								if (agents.get(nearestIndxA).valueEnergy() > meals.get(nearestIndxM).valueEnergy()) {
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
//			k =0 ;
//			for (Agent a: agents) {
//				if (!a.isKilled()) {
//					System.out.println("Agents after " + k + ": x=" + a.getX() + " y=" + a.getY() + " energy=" + a.getEnergy() + " senseRange=" + a.getSense_range() + " strength=" + a.getStrength() + " speed=" + a.getSpeed());
//					k ++ ;
//				}
//			}
			// Убить агентов
			for (Agent a: agents) {
				if (!a.isKilled()) {
					if ((a.valueEnergy() - 1 - a.getEnergyDec()) <= 0) {
						a.kill();
                        System.out.println("Agent killed by low energy" + a.getNum());
					}
				}
			}
			// Мутации генов
			for (Agent a: agents) {
				if (!a.isKilled()) {
					a.evaluateGenom(criticalA);
				}
			}

			// Мутации агентов
			//System.out.println("O:" + (agentKilled));
			Collections.sort(agents, new SortByEnergy());
			int kk = Math.round((agentNum-agentKilled)*mutationFreq);
			//System.out.println("Iteration " + i + ", Overall: " + k + ", Mutation share: " + kk);

			for (int l=0; l<kk; l++) {
				agents.get(l).evaluate();
//				System.out.println("Agents sorted " + l + ": x=" + agents.get(l).getX() + " y=" + agents.get(l).getY() + " energy=" + agents.get(l).getEnergy() + " senseRange=" + agents.get(l).getSense_range() + " strength=" + agents.get(l).getStrength() + " speed=" + agents.get(l).getSpeed());
			}

//			k =0 ;
			for (Agent a: agents) {
				if (!a.isKilled()) {
//					System.out.println("Iteration;AgentNum;         X;                  Y;              Cap;                    Size;                           ExitCost;                       Monetization;           TimeSunken");
					System.out.println(i + ";" + a.getNum() + ";" + a.getX() + ";" + a.getY() + ";" + a.valueEnergy_s()+ ";" + a.checkEnergy().size() + ";" + a.getSense_range_s() + ";" + a.getStrength_s() + ";" + a.getSpeed_s());
//					k ++ ;
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