import java.util.*;
import java.io.*;

class Production{
		int p=0;
		String LHS="";
		ArrayList<String> RHS = new ArrayList<String>();
		Set translationword =new HashSet();
		Set first=new HashSet();
		ArrayList<String> fillFirstset = new ArrayList<String>();
		ArrayList<Integer> fillFirstnu = new ArrayList<Integer>();
		ArrayList<String> tempRHS = new ArrayList<String>();
		int DerivesEmptynumber =0;
		boolean DerivesEmpty = false;
		Set Followtranslationword=new HashSet();
		Set follow = new HashSet();
		ArrayList<String> fillFollowset = new ArrayList<String>();
		ArrayList<String> fillFollowFirstset = new ArrayList<String>();
		ArrayList<Integer> fillFollowFirstnu = new ArrayList<Integer>();
		Set answer = new HashSet();
		void setp(int i){
			p=i;
		}
		void setLHS(String l){
			LHS=l;
		}
}

public class parser_4{
	public static void main(String[] args) throws IOException{
		System.out.print("Enter CFG file name:");
		Scanner sc = new Scanner(System.in);
		String FileName = sc.nextLine();
		BufferedReader br = new BufferedReader(new FileReader(FileName+".txt"));
		String line;
		boolean isRHS=false;
		ArrayList<String> grammers = new ArrayList<String>();
		ArrayList<String>  terminal= new ArrayList<String>();
		ArrayList<String>  nonterminal= new ArrayList<String>();
		
		while((line = br.readLine())!=null){
			grammers.add(line);
		}
		
		Production rule[]= new Production[grammers.size()];
		for(int i=0;i<grammers.size();i++){
          rule[i] = new Production();
		}
		
		for(int i=0;i<grammers.size();i++){
			StringTokenizer st = new StringTokenizer(grammers.get(i));
			rule[i].setp(Integer.parseInt(st.nextToken()));
				while (st.hasMoreTokens()){
					String tempstring = st.nextToken();
					if(tempstring.equals(">")){
						isRHS=true;
						tempstring = st.nextToken();
					}
					if(tempstring.equals("|")){
						rule[i].LHS=rule[i-1].LHS;
						isRHS=true;
						tempstring = st.nextToken();
					}
					if(isRHS){
						rule[i].RHS.add(tempstring);
					}
					if(!isRHS){
						rule[i].setLHS(tempstring);
					}
				}
				isRHS=false;
		}
		
		//First set
		for(int i=0;i<grammers.size();i++){
			rule[i].fillFirstnu.add(0);
			rule[i].fillFirstset.addAll(rule[i].RHS);
			
			for(int l=0;l<rule[i].fillFirstnu.size();l++){
				for(int k=0; k<grammers.size();k++){
					if(rule[i].fillFirstset.get(rule[i].fillFirstnu.get(l)).equals(rule[k].LHS)){
						if(rule[k].RHS.get(0).equals("lamda")){
							rule[i].fillFirstnu.add(rule[i].fillFirstset.size());
							for(int m=rule[i].fillFirstnu.get(l)+1;m<rule[i].fillFirstnu.get(l+1);m++){
							rule[i].fillFirstset.add(rule[i].fillFirstset.get(m));
							}
							if(!(rule[i].fillFirstnu.get(l)+1<rule[i].fillFirstnu.get(l+1))){
								rule[i].fillFirstnu.remove(rule[i].fillFirstnu.size()-1);
							}
							rule[i].translationword.add(rule[i].fillFirstset.get(rule[i].fillFirstnu.get(l)));
						}
						else{
							rule[i].fillFirstnu.add(rule[i].fillFirstset.size());
							rule[i].fillFirstset.addAll(rule[k].RHS);
							for(int m=rule[i].fillFirstnu.get(l)+1;m<rule[i].fillFirstnu.get(l+1);m++){
							rule[i].fillFirstset.add(rule[i].fillFirstset.get(m));
							}
							rule[i].translationword.add(rule[i].fillFirstset.get(rule[i].fillFirstnu.get(l)));
						}
					}
				}
			}
			if(rule[i].RHS.size()==rule[i].fillFirstset.size()){
				if(rule[i].fillFirstset.get(0).equals("lamda")){
				}
				else{	
				rule[i].first.add(rule[i].fillFirstset.get(0));
				}
			}
			else{
				for(int l=0;l<rule[i].fillFirstnu.size();l++){
				rule[i].first.add(rule[i].fillFirstset.get(rule[i].fillFirstnu.get(l)));
				}
				rule[i].first.removeAll(rule[i].translationword);
			}
			
		}
		
		//DerivesEmpty
		for(int i=0;i<grammers.size();i++){
			rule[i].tempRHS.addAll(rule[i].RHS);
		}
		
		for(int i=0;i<grammers.size();i++){
			if(rule[i].tempRHS.get(0).equals("lamda")){
			rule[i].DerivesEmpty=true;
			}
			
			for(int j=0;j<rule[i].tempRHS.size();j++){
				for(int k=0;k<grammers.size();k++){
					if((rule[i].tempRHS.get(j).equals(rule[k].LHS))&&(rule[k].RHS.get(0).equals("lamda"))){
						rule[i].DerivesEmptynumber++;
					}
					if((rule[i].tempRHS.get(j).equals(rule[k].LHS))&&(!rule[k].RHS.get(0).equals("lamda"))){
						if(!rule[i].tempRHS.contains(rule[k].LHS))
						rule[i].tempRHS.add(rule[k].LHS);
					}
				}
			}
			
			if (rule[i].DerivesEmptynumber==(rule[i].RHS.size())){
				rule[i].DerivesEmpty=true;
			}
		}
		
		//Follow Set
		
		for(int i=0;i<grammers.size();i++){
			if(rule[i].DerivesEmpty==true){
				rule[i].fillFollowset.add(rule[i].LHS);
				for(int j=0,l=0;j<rule[i].fillFollowset.size();j++){
					for(int k=0;k<grammers.size();k++){
						if((rule[k].RHS.contains(rule[i].fillFollowset.get(j)))&&((rule[k].RHS.indexOf(rule[i].fillFollowset.get(j))+1)!=rule[k].RHS.size())){
							rule[i].fillFollowFirstnu.add(rule[i].fillFollowFirstset.size());
							for(int m=rule[k].RHS.indexOf(rule[i].fillFollowset.get(j))+1; m<rule[k].RHS.size();m++){
								rule[i].fillFollowFirstset.add(rule[k].RHS.get(m));
							}
						}
						
						if((rule[k].RHS.contains(rule[i].fillFollowset.get(j)))&&((rule[k].RHS.lastIndexOf(rule[i].fillFollowset.get(j))+1)==rule[k].RHS.size())&&(!rule[k].LHS.equals(rule[i].fillFollowset.get(0)))&&(rule[k].RHS.lastIndexOf(rule[i].fillFollowset.get(j))!=rule[k].RHS.indexOf(rule[i].fillFollowset.get(j)))){
							if(rule[i].fillFollowset.get(j).equals(rule[k].LHS)){
							}
							else
							rule[i].fillFollowset.add(rule[k].LHS);
						}
						
						if((rule[k].RHS.contains(rule[i].fillFollowset.get(j)))&&((rule[k].RHS.indexOf(rule[i].fillFollowset.get(j))+1)==rule[k].RHS.size())&&(!rule[k].LHS.equals(rule[i].fillFollowset.get(0)))){
							if(rule[i].fillFollowset.get(j).equals(rule[k].LHS)){
							}
							else
							rule[i].fillFollowset.add(rule[k].LHS);
						}
					}	
				}

				//System.out.println(rule[i].LHS+" "+rule[i].fillFollowFirstset+" "+rule[i].fillFollowFirstnu);
				
				for(int l=0;l<rule[i].fillFollowFirstnu.size();l++){
					for(int k=0; k<grammers.size();k++){
						if(rule[i].fillFollowFirstset.get(rule[i].fillFollowFirstnu.get(l)).equals(rule[k].LHS)){
							if(rule[k].RHS.get(0).equals("lamda")){
								rule[i].fillFollowFirstnu.add(rule[i].fillFollowFirstset.size());
								for(int m=rule[i].fillFollowFirstnu.get(l)+1;m<rule[i].fillFollowFirstnu.get(l+1);m++){
									rule[i].fillFollowFirstset.add(rule[i].fillFollowFirstset.get(m));
								}
								if(!(rule[i].fillFollowFirstnu.get(l)+1<rule[i].fillFollowFirstnu.get(l+1))){
								rule[i].fillFollowFirstnu.remove(rule[i].fillFollowFirstnu.size()-1);
								}
								rule[i].Followtranslationword.add(rule[i].fillFollowFirstset.get(rule[i].fillFollowFirstnu.get(l)));
							}
							else{
								rule[i].fillFollowFirstnu.add(rule[i].fillFollowFirstset.size());
								rule[i].fillFollowFirstset.addAll(rule[k].RHS);
								for(int m=rule[i].fillFollowFirstnu.get(l)+1;m<rule[i].fillFollowFirstnu.get(l+1);m++){
								rule[i].fillFollowFirstset.add(rule[i].fillFollowFirstset.get(m));
								}
								rule[i].Followtranslationword.add(rule[i].fillFollowFirstset.get(rule[i].fillFollowFirstnu.get(l)));
							}
						}
					}
				}
				//System.out.println(rule[i].LHS+" "+rule[i].fillFollowFirstset+" "+rule[i].fillFollowFirstnu);
				
				for(int l=0;l<rule[i].fillFollowFirstnu.size();l++){
					rule[i].follow.add(rule[i].fillFollowFirstset.get(rule[i].fillFollowFirstnu.get(l)));
				}
				rule[i].follow.removeAll(rule[i].Followtranslationword);	
			}
		}
		
		//answer
		for(int i=0;i<grammers.size();i++){
			rule[i].answer.addAll(rule[i].first);
			rule[i].answer.addAll(rule[i].follow);
		}
		
		//LL table
		for(int i=0;i<grammers.size();i++){
			if(!nonterminal.contains(rule[i].LHS))
			nonterminal.add(rule[i].LHS);
		}
		for(int i=0;i<grammers.size();i++){
			for(int j=0;j<rule[i].RHS.size();j++){
				if(!terminal.contains(rule[i].RHS.get(j))){
				terminal.add(rule[i].RHS.get(j));
				}
			}
		}
		
		terminal.removeAll(nonterminal);
		terminal.remove("lamda");
		terminal.remove("$");
		terminal.add("$");
		
		int table[][] = new int[nonterminal.size()][terminal.size()];
		
		for(int i=0 ;i<nonterminal.size();i++){
			for(int j=0; j<terminal.size();j++){
				for(int k=0;k<grammers.size();k++){
					if(rule[k].LHS==nonterminal.get(i)){
							if(rule[k].answer.contains(terminal.get(j)))
							table[i][j]=rule[k].p;
					}
				}
			}
		}
		/*
		for(int i=0;i<grammers.size();i++){
			if(rule[i].DerivesEmpty==true)
			System.out.println(rule[i].LHS+"\t"+rule[i].answer);
			else
			System.out.println(rule[i].LHS+"\t"+rule[i].answer);
		}
		*/
		for(int i=0;i<terminal.size();i++){
		System.out.print("\t"+terminal.get(i));
		}
		System.out.print("\n");
		
		for(int i=0;i<nonterminal.size();i++){
		System.out.print(nonterminal.get(i)+"\t");
		for(int j=0;j<terminal.size();j++){
				if(table[i][j]==0)
				System.out.print("\t");
				else			
				System.out.print(table[i][j]+"\t");
			 }
		System.out.print("\n");
		}
		System.out.println("===================================================");
		
		//inputString find in map
		System.out.print("Input token string: ");
		String intputString = sc.nextLine();
		StringTokenizer input = new StringTokenizer(intputString);
		ArrayList<String> parserStack = new ArrayList<String>();
		ArrayList<String> remainingInput = new ArrayList<String>();
		while(input.hasMoreTokens()){
			remainingInput.add(input.nextToken());
		}
		parserStack.add(rule[0].LHS);
		System.out.print("Apply rule:");
		while(!remainingInput.isEmpty()){
			if(remainingInput.get(0).equals(parserStack.get(0))){
				remainingInput.remove(0);
				parserStack.remove(0);
				if(remainingInput.isEmpty()){
					System.exit(0);
				}
			}
			
			if(!terminal.contains(remainingInput.get(0))){
				System.out.println(" Error");
				System.exit(0);
			}
			
			for(int k=0;k<nonterminal.size();k++){
				for(int j=0; j< terminal.size();j++){
					if(remainingInput.get(0).equals(terminal.get(j))&&parserStack.get(0).equals(nonterminal.get(k))){
						if(table[k][j]==0){
							System.out.println(" Error");
							System.exit(0);
						}
						else if(table[k][j]!=0){
							parserStack.remove(0);
							if(rule[table[k][j]-1].RHS.get(0).equals("lamda")){
								System.out.print(" "+table[k][j]);
							}
							else{
								for(int l=rule[table[k][j]-1].RHS.size()-1;l>=0;l--){
									parserStack.add(0,rule[table[k][j]-1].RHS.get(l));
								}
								System.out.print(" "+table[k][j]);
							}
						}
					}
				}
			}
			
			if(!terminal.contains(remainingInput.get(0))){
				System.out.println(" Error");
				System.exit(0);
			}
			
			if(!nonterminal.contains(parserStack.get(0))&&!remainingInput.get(0).equals(parserStack.get(0))){
				System.out.println(" Error");
				System.exit(0);
			}
		}
	}
}