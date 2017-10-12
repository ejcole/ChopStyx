package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import algorithms.abstracts.Action;

public class CSVMoveWriter
{
	public static void writeToCSV_2Hand(HashMap<StickState2H, Action> stateToMoveMap, String fileNameWithExtension)
	{
		File tmpDir = new File(fileNameWithExtension);
		if(tmpDir.exists())
		{
			System.err.println("File exists, preventing file overwrite: " + fileNameWithExtension);
			return;
		}
		
		try
		{
			FileWriter fw = new FileWriter(fileNameWithExtension, false);
			BufferedWriter bw = new BufferedWriter(fw);
			StringBuilder sb = new StringBuilder();
			sb.append("state,action\n");
			for(StickState2H state : stateToMoveMap.keySet())
			{
				ActionSticks2H action =  (ActionSticks2H) stateToMoveMap.get(state);
				String actionStr = action.getString(state.move);
				String stateStr = state.getFileString();
				String entry = stateStr + "," + actionStr + "\n";
				sb.append(entry);
			}
			//System.out.println(sb.toString());
			bw.write(sb.toString());
			bw.close();
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("failed to write to submission file");
		}
	}
	
	public static HashMap<StickState2H, Action> convert2HandFileToMap(String fileNameWithExtension) throws FileNotFoundException
	{
		File file = new File(fileNameWithExtension);
		Scanner fileScanner = new Scanner(file);

		StringBuilder sb = new StringBuilder();
		while (fileScanner.hasNextLine())
		{
			sb.append(fileScanner.nextLine() + '\n');
		}
		fileScanner.close();

		String contents = sb.toString();
		String[] rawLines = contents.split("\n");

		HashMap<StickState2H, Action> moveTable = new HashMap<>();
		for(int i = 1; i < rawLines.length; ++i)
		{
			String line = rawLines[i];
			String[] stateThenAction = line.split(",");
			
			StickState2H state = StickState2H.parseString(stateThenAction[0]);
			ActionSticks2H action = ActionSticks2H.parseAction(stateThenAction[1]);
			moveTable.put(state, action);
		}
		
		fileScanner.close();
		return moveTable;
	}
}












