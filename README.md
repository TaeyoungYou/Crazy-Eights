<div align="center">
<h2>[2025] 🂡 Game Development Assignment 🂡</h2>
Crazy Eights is a fun card game that you can enjoy solo or in multiplayer with friends! <br>
It is designed to let you play while chatting with friends and enjoying relaxing music.
</div>

## Contents
- [Contents](#contents)
- [Overview](#overview)
- [Explain Game](#explain-game)

## Overview
- Project Name: Crazy Eights 🃏
- Project Date: Winter, 2025 at Algonquin College
- Engine & Langugae: JavaFX in Java
- Member: Taeyoung You

## Explain Game
|![image](https://github.com/user-attachments/assets/8bfde698-3842-4d3f-92d4-1b61cb634a21)|![image](https://github.com/user-attachments/assets/8021164b-5f73-4b17-b4bd-94cff1c7b8ca)|![image](https://github.com/user-attachments/assets/c7fb07f8-be16-4e13-b8f0-9507c409dfe4)|
|:---:|:---:|:---:|
|Main Menu|Single Play Window|Setting|

### **Game Rules:**

1. **Starting Hand:**  
   - Each player starts with **6 cards**.

2. **Turn Order:**  
   - The turn order follows a **clockwise direction**.  
   - The first player is determined by the **lowest score** in the score box.

3. **Player's Turn:**  
   A player must do one of the following actions on their turn:  
   - **Play a card:**  
     - Regular card → **Hand -1**  
     - **8-card** → **Hand -1** + Change the shape  
   - **Draw a card:**  
     - If the player has **no matching number or shape**, they must draw **1 card**.  
     - **Cannot draw if hand size exceeds 12**.

4. **Game Continuation & End Condition:**  
   - The game continues until **one player has no cards left (Hand == 0)**.  
   - The remaining players are ranked based on their remaining cards.  
   - **Score Adjustment:**  
     - **1st place**: +5 points  
     - **2nd place**: +4 points  
     - **3rd place**: +3 points  
     - **4th place**: +2 points
