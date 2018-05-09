package datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import bean.dbmodel.ChanceCacheModel;
import bean.dbmodel.CollegeModel;
import bean.requestresult.CollegeChanceResult;

public class ChanceUtil {
	public static Logger logger1 = Logger.getLogger(ChanceUtil.class);
	private static Random random = new Random(System.currentTimeMillis());

	public List<CollegeChanceResult> getChanceList(List<CollegeModel> colleges_all, boolean isWen, int score) {
		List<CollegeChanceResult> chanceResultList = new ArrayList<CollegeChanceResult>();

		String key = score + "_" + isWen;
		ChanceCacheModel chanceCache = ChanceCacheModel.dao.findFirst("select * from chance_cache where cacheKey = ?", key);

		if (chanceCache == null) {

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < colleges_all.size(); i++) {
				CollegeChanceResult chanceResult = new CollegeChanceResult();
				chanceResult.setCollege(colleges_all.get(i));

				int diff = 0;
				if (isWen) {
					diff = score - Integer.parseInt(chanceResult.getCollege().getWen_2017());
				} else {
					diff = score - Integer.parseInt(chanceResult.getCollege().getLi_2017());
				}

				// 0-5 30%-40%
				// 6-10 40%-60%
				// 11-15 60%-80%
				// 15-20 80%-90%
				
				if(diff == -1) {
					chanceResult.setChance(75 + random.nextInt(5));
				}
				if(diff == -2) {
					chanceResult.setChance(70 + random.nextInt(4));
				}
				if(diff == -3) {
					chanceResult.setChance(60 + random.nextInt(10));
				}
				if(diff == -4) {
					chanceResult.setChance(50 + random.nextInt(10));
				}
				if(diff == -5) {
					chanceResult.setChance(40 + random.nextInt(10));
				}
				
				if(diff >= 0 && diff <= 20) {
					chanceResult.setChance(88 + diff / 2);
				}
				if(diff >20) {
					chanceResult.setChance(99);
				}
				
				sb.append(chanceResult.getChance());
				if (i != colleges_all.size() - 1) {
					sb.append(",");
				}

				System.out.println("diff:" + diff + " " + chanceResult.getChance() + "%");
				chanceResultList.add(chanceResult);
			}

			if (chanceCache == null) {
				chanceCache = new ChanceCacheModel();
				chanceCache.setCacheKey(key);
				chanceCache.setChances(sb.toString());
				chanceCache.save();
			}
		} else {
			String chances = chanceCache.getChances();
			if (chances == null || "".equals(chances)) {
				logger1.error("Chance " + key + " is NULL");
				removeKey(key);
			} else {
				String[] chanceArray = chances.split(",");
				if (chanceArray.length != colleges_all.size()) {
					logger1.error("Chance " + key + " CacheCount:" + chanceArray.length + " collegeListSize:"
							+ colleges_all.size() + " NOT EQUALS");
					removeKey(key);
				} else {
					for (int i = 0; i < colleges_all.size(); i++) {
						CollegeChanceResult chanceResult = new CollegeChanceResult();
						chanceResult.setCollege(colleges_all.get(i));
						chanceResult.setChance(Integer.parseInt(chanceArray[i]));
						chanceResultList.add(chanceResult);
					}
				}
			}
		}
		
		return chanceResultList;
	}

	private void removeKey(String key) {
		ChanceCacheModel chanceCache = ChanceCacheModel.dao.findFirst("select * from chance_cache where key = ?", key);
		if (chanceCache != null) {
			chanceCache.delete();
		}
	}

}
