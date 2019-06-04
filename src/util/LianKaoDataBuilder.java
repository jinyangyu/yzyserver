package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.dbmodel.LiankaoScoreModel;

public class LianKaoDataBuilder {

	private static LianKaoDataBuilder instance = new LianKaoDataBuilder();
	private static final String PATH = "/Users/yujinyang11/Desktop/yzy_data/liankao/";

	private LianKaoDataBuilder() {
	}

	public static LianKaoDataBuilder getInstance() {
		return instance;
	}

	public void init() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATH + "5_jiaozuo2_wen.txt"));
			String line = null;

			int liankaoRank = 1;

			int preRank = 0;
			int rankCount = 0;

			List<LiankaoScoreModel> modelList = new ArrayList<LiankaoScoreModel>();
			LiankaoScoreModel model = null;

			while ((line = reader.readLine()) != null) {
				String[] strArray = line.split("\t");
				int rank = Integer.parseInt(strArray[1]);
				int score = Integer.parseInt(strArray[0]);

				if (preRank != rank) {
					modelList.add(model);
					model = new LiankaoScoreModel();
					model.setRank(liankaoRank++);
					model.setWenli("wen");
					model.setLiankao_id(5);
					model.setSchool("wxyz");
					model.setScore(score);

					preRank = rank;
					rankCount = 0;
				}

				rankCount++;
				model.setCount(rankCount);

				System.out.println("score:" + score + " rank:" + rank + " count:" + rankCount);
			}

			for (LiankaoScoreModel m : modelList) {
				if (m != null) {
					m.save();
					System.out.println("model score:" + m.getScore() + " rank:" + m.getRank() + " count:" + m.getCount()
							+ " wenli:" + m.getWenli() + " school:" + m.getSchool());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
