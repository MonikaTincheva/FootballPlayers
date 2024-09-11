# FootballPlayers

Logic of the Algorithm:


1.	Initialization:
      -	First, I retrieve all the records from the recordRepository, which represent the playing times of players in different matches.
      -	I initialize two maps:

           - pairs: This map stores the total time that two players (a pair) have played together across all matches.

           - pairRecordPlay: This map stores detailed information about the time each pair played together in each individual match.


2.	Nested Loop to Compare Records:
      -	I use a nested loop to compare each record with every other record.
      -	For each pair of records (record and record2), I check if:
    
           - They belong to the same match. 

           - They are not the same player.

           - They are from different teams.

      - If these conditions are met, I calculate how much time these two players have played together in that match.

3. Calculate Time Played Together:
      -	I use the method timeOfPlayedTogether() to compute the overlapping minutes between the two players. If the overlap is greater than 0, meaning they played together during the match, I proceed to update the data for this pair.

4. Find the Maximum Play Time:
      -	After processing all records, I search the pairs map to find the pair with the maximum play time.
      -	I filter the pairs to get all pairs that have the same maximum play time.

5. Build the Output:
   - For each pair that has played together the maximum amount of time, I append their player IDs and the total time they played together to the result.

   - Then, I loop through the detailed match records stored in pairRecordPlay to append the match IDs and the time they played together in each match. 

6. Return Result:
    -	The method returns a string that lists: 

         - The player IDs and their total play time together. 

         - The match IDs and the play time for each match.

  - If no players played together in any matches, the method returns a message stating that no players were found who played in the same match.
      

________________________________________
Example Output:
- If players 113 and 128 played together for a total of 84 minutes across several matches, the output might look like this:
    - 113, 128, 84
  - 101, 30
  - 54, 54
- This means:
    - Player 113 and Player 128 played together for a total of 84 minutes. 
    - In match 101, they played 30 minutes together.

    - In match 54, they played 54 minutes together.




