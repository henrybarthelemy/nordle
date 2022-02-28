import javalib.worldimages.*;
import javalib.funworld.*;

import java.awt.Color;
import java.util.Random;

//Holds all the contents refering to the rendering of Nordle
interface IRenderingConstants {
  int SCREEN_HEIGHT = 700; // Screen height for big bang
  int SCREEN_WIDTH = 700; // Screen width for big bang
  int BLOCK_HEIGHT = 50; // A block's height (letter block & typing block)
  int BLOCK_WIDTH = 50; // A block's width (letter block & typing block)
  Color CORRECT_LETTER_COLOR = Color.GREEN; // Color for correct letter block
  Color MISPLACED_LETTER_COLOR = Color.YELLOW; // Color for misplaced letter block
  Color INCORRECT_LETTER_COLOR = Color.GRAY; // Color for incorrect letter block
  Color TYPED_BLOCK = Color.LIGHT_GRAY; // Color for typing block
  Color TEXT_COLOR = Color.BLACK; // Color text should be
  Color BACKGROUND_COLOR = Color.WHITE; // Background color of scene
}

public class Starter implements IRenderingConstants {
  public static void main(String[] args) {
    IList<IList<LetterBlock>> emptyLetterBlock = new MtLo<IList<LetterBlock>>();
    IList<String> mtLString = new MtLo<String>();
    
    WordList wl = new WordList();
    IList<String> wordListLength5 = wl.split(wl.fiveLetters, 5, 1);
    
    Dordle randomDord = new Dordle(new Wordle(0, emptyLetterBlock, mtLString, wordListLength5),
        new Wordle(0, emptyLetterBlock, mtLString, wordListLength5));

    Dordle ww = randomDord; // INIT WORLD STATE HERE (either theWordleInit or dordleInit)
    int worldWidth = SCREEN_WIDTH;
    int worldHeight = SCREEN_HEIGHT;
    double tickRate = 20;
    ww.bigBang(worldWidth, worldHeight, tickRate);
  }


}

//A list of generic type t
interface IList<T> {
  int len(); // calculates the length of this list

  <R> IList<R> map(IFunc<T, R> func); // maps this list to another

  T getListItem(int n); // gets the nth value of this list
  // or an error if the n value is greater than the length
  // of the list

  // Applies func towards left of list, returns final current value

  // ACCUMULATES: Accumulates on the base case (current) applying t to modify r
  // with func
  // (from right to left to list<T>), returns the final R
  // TERMINATES: Returns the final "current" when list if empty
  <R> R foldl(IFoldFunc<T, R> func, R current);

  // A foldish function that applies a function to a list, while also incrementing
  // a counter
  // keeping track of the amount of times it has been called
  // ACCUMULATE: Accumulates the amount of times the function has been called
  // TERMINATES: Terminates when it hits an empty list
  <R> R foldlWithCounter(IFoldFuncWithCounter<T, R> func, int count, R current);

  // ACCUMULATES: Accumulates on the base case (current) applying t to modify r
  // with func
  // (from left to right to list<T>), returns the final R
  // TERMINATES: Returns the final "current" when list if empty
  <R> R foldr(IFoldFunc<T, R> func, R current);

  // Is this list the same as the given list?
  boolean sameList(IList<T> other);

  // Is this list the same as the given cons list?
  boolean sameCons(ConsLo<T> other);

  // Is this list the same as the given empty list?
  boolean sameMt(MtLo<T> other);

  // Is the given item in this given list
  boolean contains(T item);

  // Appends this list to the given list
  IList<T> append(IList<T> other);

  // Reverses a list
  IList<T> reverse();

  // Removes the first item from this list.
  IList<T> removeFirst();

}

//A Cons of a T onto a list of T
class ConsLo<T> implements IList<T> {
  T first; // First element of type t in this list
  IList<T> rest; // Rest of this type t list

  ConsLo(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * Template
   * 
   * Fields: this.first -- T this.rest -- List<T>
   * 
   * Methods: this.len() -- int this.map(IFunc<T, R>) -- IList<R>
   * this.getListItem(int n) -- T <R> this.rest.foldl(IFoldFunc<T, R> func, R
   * current) -- R <R> this.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
   * count, R current) -- R <R> this.rest.foldr(IFoldFunc<T, R> func, R current)
   * -- R this.sameList(IList<T> other) - boolean this.sameCons(ConsLo<T> other) -
   * boolean this.sameMt(MtLo<T> other) - boolean this.contains(T item) - boolean
   * this.append(IList<T> other) - IList<T> this.reverse() - IList<T>
   * this.removeFirst() - IList<T>
   * 
   * Methods on fields: this.rest.len() -- int this.rest.map(IFunc<T, R>) --
   * IList<R> this.rest.getListItem(int n) -- T <R>
   * this.rest.rest.foldl(IFoldFunc<T, R> func, R current) -- R <R>
   * this.rest.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int count, R
   * current) -- R <R> this.rest.rest.foldr(IFoldFunc<T, R> func, R current) -- R
   * this.rest.sameList(IList<T> other) - boolean this.rest.sameCons(ConsLo<T>
   * other) - boolean this.rest.sameMt(MtLo<T> other) - boolean
   * this.rest.contains(T item) - boolean this.rest.append(IList<T> other) -
   * IList<T> this.rest.reverse() - IList<T> this.rest.removeFirst() - IList<T>
   */

  // Calculates the length of this cons list
  public int len() {
    return 1 + this.rest.len();
  }

  // Maps this list to another list according to a function
  public <R> IList<R> map(IFunc<T, R> func) {

    /*
     * Template Methods on parameters: --func.apply(T parameter)-- --R
     */

    return new ConsLo<R>(func.apply(first), this.rest.map(func));
  }

  // Gets the nth value of this list or errors out if the n is out of index
  public T getListItem(int n) {
    if (n == 0) {
      return this.first;
    }
    else {
      return rest.getListItem(n - 1);
    }
  }

  // Applies the function from the left to the right of the list
  public <R> R foldl(IFoldFunc<T, R> func, R current) {
    return this.rest.foldl(func, func.apply(first, current));
  }

  // Applies the function from left to right while incrementing the counter of
  // times its been called
  public <R> R foldlWithCounter(IFoldFuncWithCounter<T, R> func, int count, R current) {
    return this.rest.foldlWithCounter(func, count + 1, func.apply(first, count, current));
  }

  // Applies the function from the right to the left of the list
  public <R> R foldr(IFoldFunc<T, R> func, R current) {
    // Applies foldl on the verse of this lists
    return this.reverse().foldl(func, current);
  }

  // Is this list the same as the given list?
  public boolean sameList(IList<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return other.sameCons(this);
  }

  // Is this list the same as the given cons list?
  public boolean sameCons(ConsLo<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return this.first.equals(other.first) && this.rest.sameList(other.rest);
  }

  // Is this list the same as the given empty list?
  public boolean sameMt(MtLo<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return false;
  }

  // Is the item in this list?
  public boolean contains(T item) {
    return this.first.equals(item) || this.rest.contains(item);
  }

  // Appends this list to the given list
  public IList<T> append(IList<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return new ConsLo<T>(this.first, this.rest.append(other));
  }

  // Reverses a list
  public IList<T> reverse() {
    return this.rest.reverse().append(new ConsLo<T>(this.first, new MtLo<T>()));
  }

  // Removes the first item from this list.
  public IList<T> removeFirst() {
    return this.rest;
  }

}

//An empty list of type T
class MtLo<T> implements IList<T> {

  /*
   * Template
   * 
   * Fields: N/A
   * 
   * Methods: this.len() -- int this.map(IFunc<T, R>) -- IList<R>
   * this.getListItem(int n) -- T <R> this.rest.foldl(IFoldFunc<T, R> func, R
   * current) -- R <R> this.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
   * count, R current) -- R <R> this.rest.foldr(IFoldFunc<T, R> func, R current)
   * -- R this.sameList(IList<T> other) - boolean this.sameCons(ConsLo<T> other) -
   * boolean this.sameMt(MtLo<T> other) - boolean this.contains(T item) - boolean
   * this.append(IList<T> other) - IList<T> this.reverse() - IList<T>
   * this.removeFirst() - IList<T>
   * 
   * Methods on fields: N/A
   */

  // calculates the length of this list
  public int len() {
    return 0;
  }

  // Maps this list to another list according to a function
  public <R> IList<R> map(IFunc<T, R> func) {
    return new MtLo<R>();
  }

  // Gets the nth value of this list or errors out if the n is out of index
  public T getListItem(int n) {
    throw new RuntimeException("Tried to find nth where N is out of index of the list");
  }

  // Applies the function from the left to the right of the list
  public <R> R foldl(IFoldFunc<T, R> func, R current) {
    return current;
  }

  // Applies the function from the right to the left of the list
  public <R> R foldr(IFoldFunc<T, R> func, R current) {
    return current;
  }

  // Applies the function from left to right while incrementing the counter of
  // times its been called
  public <R> R foldlWithCounter(IFoldFuncWithCounter<T, R> func, int count, R current) {
    return current;
  }

  // Is this list the same as the given list?
  public boolean sameList(IList<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return other.sameMt(this);
  }

  // Is this list the same as the given cons list?
  public boolean sameCons(ConsLo<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return false;
  }

  // Is this list the same as the given empty list?
  public boolean sameMt(MtLo<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return true;
  }

  // Is the item in this list?
  public boolean contains(T item) {
    return false;
  }

  // Appends this list to the given list
  public IList<T> append(IList<T> other) {
    /*
     * Methods on parameters other.len() -- int other.map(IFunc<T, R>) -- IList<R>
     * other.getListItem(int n) -- T <R> other.rest.foldl(IFoldFunc<T, R> func, R
     * current) -- R <R> other.foldlWithCounter(IFoldFuncWithCounter<T, R> func, int
     * count, R current) -- R <R> other.rest.foldr(IFoldFunc<T, R> func, R current)
     * -- R other.sameList(IList<T> other) - boolean other.sameCons(ConsLo<T> other)
     * - boolean other.sameMt(MtLo<T> other) - boolean other.contains(T item) -
     * boolean other.append(IList<T> other) - IList<T> other.reverse() - IList<T>
     * other.removeFirst() - IList<T>
     */
    return other;
  }

  // Reverses a list
  public IList<T> reverse() {
    return this;
  }

  // Removes the first item from this list.
  public IList<T> removeFirst() {
    return this;
  }
}

//<T,R> Applying function with paramater type T and return type R
interface IFunc<T, R> {
  // Apply this function object on t
  R apply(T parameter);
}

//A foldr or foldl function
interface IFoldFunc<A1, R> {
  // Apply this function object with arg1 and arg2, with return type same as arg 2
  R apply(A1 arg1, R arg2);
}

//A foldr or foldl function with an counter that increments every time its called
interface IFoldFuncWithCounter<A1, R> {
  // Apply this function object with arg1 and an increment int, with return type
  // same as toReturn
  R apply(A1 arg1, int increment, R toReturn);
}

//Basic Fold function that adds together parameters
class AddFoldFunc implements IFoldFunc<Integer, Integer> {
  // Applies this function object with int1 and int2, with return type Integer
  public Integer apply(Integer int1, Integer int2) {
    return int1 + int2;
  }
}

//A Fold function that concatenates strings 
class ConcatenateString implements IFoldFunc<String, String> {
  // Applies this function object with str1 and str2, with return type String
  public String apply(String str1, String str2) {
    return str1 + str2;
  }
}

//A Special fold function that creates a list of letter blocks given 
//a secret word constant, and current index
class CompareGuess implements IFoldFuncWithCounter<String, IList<LetterBlock>> {
  String secretWord; // Word to compare guess to

  CompareGuess(String secretWord) {
    this.secretWord = secretWord;
  }

  /*
   * Template: Fields: this.secretWord -- String Methods: this.apply(String,
   * Integer, IList<LetterBlock>) -- IList<LetterBlock> Methods on Fields:
   * this.secretWord.subString(int) -- String this.secretWord.subString(int) --
   * String this.secretWord.contains(String) -- boolean
   * 
   */

  // Applies this function object to a letter, an index, and a current row,
  // returning an IList<LetterBlock>
  public IList<LetterBlock> apply(String letter, int index, IList<LetterBlock> curNewGuessRow) {
    // Cases: 3
    // - secretWord contains letter, but letter not in right place of secretWord
    // - letter in right place of secretWord
    // - letter not in secretWord at all

    /*
     * Methods on Parameters: curNewGuessRow.len() -- int
     * curNewGuessRow.map(IFunc<LetterBlock, R>) -- IList<R>
     * curNewGuessRow.getListItem(int n) -- LetterBlock <R>
     * curNewGuessRow.rest.foldl(IFoldFunc<LetterBlock, R> func, R current) -- R <R>
     * curNewGuessRow.foldlWithCounter( IFoldFuncWithCounter<LetterBlock, R> func,
     * int count, R current) -- R <R>
     * curNewGuessRow.rest.foldr(IFoldFunc<LetterBlock, R> func, R current) -- R
     * curNewGuessRow.sameList(IList<LetterBlock> other) - boolean
     * curNewGuessRow.sameCons(ConsLo<LetterBlock> other) - boolean
     * curNewGuessRow.sameMt(MtLo<LetterBlock> other) - boolean
     * curNewGuessRow.contains(LetterBlock item) - boolean
     * curNewGuessRow.append(IList<LetterBlock> other) - IList<LetterBlock>
     * curNewGuessRow.reverse() - IList<LetterBlock> curNewGuessRow.removeFirst() -
     * IList<LetterBlock>
     */

    String letterSecretWord; // null until assigned to substring of letter
    if (this.secretWord.contains(letter)) {
      // Letter is in the word, need to check whether its in the correct positon
      // Two types of substring depending on whether were at the last index (avoids
      // out of index)
      if (this.secretWord.length() <= index) {
        letterSecretWord = this.secretWord.substring(index);
      }
      else {
        letterSecretWord = this.secretWord.substring(index, index + 1);
      }

      // Add's letterblock to list according to whether its in the correct positon
      if (letterSecretWord.equals(letter)) {
        return new ConsLo<LetterBlock>(new LetterBlock(letter, 0), curNewGuessRow); // correct pos
      }
      else {
        return new ConsLo<LetterBlock>(new LetterBlock(letter, 1), curNewGuessRow); // incorrect pos
      }
    }
    else {
      // Letter is in the not in word
      // return letterblock of letter with -1 to current processed guess row
      return new ConsLo<LetterBlock>(new LetterBlock(letter, -1), curNewGuessRow);
    }
  }
}

//Renders the current guess and places the rest of the guesses above it
class AboveSingleGuessFunc implements IFoldFunc<IList<LetterBlock>, WorldImage> {

  /*
   * Template: Fields: N/A Methods this.apply(IList<LetterBlock>, WorldImage) --
   * WorldImage this.renderCurrentGuess(IList<LetterBlock> guess) -- WorldImage
   * Methods on Fields: N/A
   */

  // Renders all the guesses, and places them above/below eachother
  public WorldImage apply(IList<LetterBlock> guess, WorldImage img2) {
    /*
     * Methods on Parameters: guess.len() -- int guess.map(IFunc<LetterBlock, R>) --
     * IList<R> guess.getListItem(int n) -- LetterBlock <R>
     * guess.rest.foldl(IFoldFunc<LetterBlock, R> func, R current) -- R <R>
     * guess.foldlWithCounter( IFoldFuncWithCounter<LetterBlock, R> func, int count,
     * R current) -- R <R> guess.rest.foldr(IFoldFunc<LetterBlock, R> func, R
     * current) -- R guess.sameList(IList<LetterBlock> other) - boolean
     * guess.sameCons(ConsLo<LetterBlock> other) - boolean
     * guess.sameMt(MtLo<LetterBlock> other) - boolean guess.contains(LetterBlock
     * item) - boolean guess.append(IList<LetterBlock> other) - IList<LetterBlock>
     * guess.reverse() - IList<LetterBlock> guess.removeFirst() - IList<LetterBlock>
     */
    return new AboveImage(img2, renderCurrentGuess(guess));
  }

  // Returns the image for a single guess
  WorldImage renderCurrentGuess(IList<LetterBlock> guess) {
    /*
     * Methods on Parameters: guess.len() -- int guess.map(IFunc<LetterBlock, R>) --
     * IList<R> guess.getListItem(int n) -- LetterBlock <R>
     * guess.rest.foldl(IFoldFunc<LetterBlock, R> func, R current) -- R <R>
     * guess.foldlWithCounter( IFoldFuncWithCounter<LetterBlock, R> func, int count,
     * R current) -- R <R> guess.rest.foldr(IFoldFunc<LetterBlock, R> func, R
     * current) -- R guess.sameList(IList<LetterBlock> other) - boolean
     * guess.sameCons(ConsLo<LetterBlock> other) - boolean
     * guess.sameMt(MtLo<LetterBlock> other) - boolean guess.contains(LetterBlock
     * item) - boolean guess.append(IList<LetterBlock> other) - IList<LetterBlock>
     * guess.reverse() - IList<LetterBlock> guess.removeFirst() - IList<LetterBlock>
     */
    IList<WorldImage> letterBlockImageList = guess.map(new LetterBlockToImage());
    return letterBlockImageList.foldl(new BesideFoldFunc(), new EmptyImage());
  }

}

//Places the two images besides each other
class BesideFoldFunc implements IFoldFunc<WorldImage, WorldImage> {
  /*
   * Template: Fields: N/A Methods this.apply(WorldImage, WorldImage) --
   * WorldImage Methods on Fields: N/A
   */

  // Applies this function object to a WorldImage right, WorldImage left,
  // returning a WorldImage
  public WorldImage apply(WorldImage right, WorldImage left) {
    return new BesideImage(left, right);
  }
}

//Function used to test win condition, are all the letter blocks 0 / correct?
//Think and-map of all 1's for letterblock list
class AllOneLBFoldFunc implements IFoldFunc<LetterBlock, Boolean> {
  /*
   * Template: Fields: N/A Methods: this.apply(LetterBlock, Boolean) -- Boolean
   * Methods on Fields: N/A
   */

  // Applies this function object to a LetterBlock current, a
  // Boolean so far and returns a Boolean
  public Boolean apply(LetterBlock current, Boolean soFar) {
    /*
     * Methods on parameters current.renderLetterBlock() -- WorldImage
     * current.renderLetter() -- WorldImage current.renderBlock() -- WorldImage
     */
    return (current.right == 0) && soFar;
  }
}

//LetterBlockToImage function that converts a letter block to an image
// by applying its rendering method
class LetterBlockToImage implements IFunc<LetterBlock, WorldImage> {
  /*
   * Template: Fields: N/A Methods: this.apply(LetterBlock) -- WorldImage Methods
   * on Fields: N/A
   * 
   */

  // Applies this function object to a LetterBlock parameter, a
  // and returns a WorldImage
  public WorldImage apply(LetterBlock parameter) {
    /*
     * Methods on parameters
     * 
     * parameter.renderLetterBlock() -- WorldImage parameter.renderLetter() --
     * WorldImage parameter.renderBlock() -- WorldImage
     */
    return parameter.renderLetterBlock();
  }
}

//TypingBlockToImage function that converts a typing block to an image
//by applying its rendering method
class TypingBlockToImage implements IFunc<TypingBlock, WorldImage> {
  /*
   * Template: Fields: N/A Methods: this.apply(TypingBlock) -- WorldImage Methods
   * on Fields: N/A
   */

  // Applies this function object to a TypingBlock parameter, a
  // and returns a WorldImage
  public WorldImage apply(TypingBlock parameter) {
    /*
     * Methods on parameters parameter.renderLetterBlock() -- WorldImage
     * parameter.renderLetter() -- WorldImage parameter.renderBlock() -- WorldImage
     */
    return parameter.renderLetterBlock();
  }
}

//String to typing block that converts a string to a typing block by
// creating a typing block with the string
class StringToTypingBlock implements IFunc<String, TypingBlock> {
  /*
   * Template: Fields: N/A Methods: this.apply(String) -- TypingBlock Methods on
   * Fields: N/A
   */

  public TypingBlock apply(String letter) {
    return new TypingBlock(letter);
  }
}

//Represents a pair of two items 
class Pair<L, R> {
  L left; // The left item in this pair
  R right; // The right item in this pair

  /*
   * Template: Fields: this.left -- L this.right -- R Methods: N/A Methods on
   * Fields: N/A
   */

  Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }
}

// Represents a letter the user types
class TypingBlock implements IRenderingConstants {
  String letter; // The letter the user is typing

  TypingBlock(String letter) {
    this.letter = letter;
  }

  // For an empty board square
  TypingBlock() {
    this("");
  }

  /*
   * Template
   * 
   * Fields: this.letter -- String
   * 
   * 
   * Methods:
   * 
   * this.renderLetterBlock() -- WorldImage this.renderLetter() -- WorldImage
   * this.renderBlock() -- WorldImage
   * 
   * 
   * 
   */

  // Renders this LetterBlock to an image
  WorldImage renderLetterBlock() {
    return new OverlayImage(this.renderLetter(), this.renderBlock());
  }

  // Renders the letter part of this to an image
  WorldImage renderLetter() {
    return new TextImage(this.letter, TEXT_COLOR);
  }

  // Renders a block according to the width and length
  WorldImage renderBlock() {
    return new RectangleImage(BLOCK_WIDTH, BLOCK_HEIGHT, OutlineMode.SOLID, TYPED_BLOCK);
  }
}

// Represents a pair of a letter and whether it is in the secret code or not
class LetterBlock extends Pair<String, Integer> implements IRenderingConstants {

  LetterBlock(String letter, Integer guessType) {
    super(letter, guessType);
  }

  LetterBlock(Pair<String, Integer> pair) {
    super(pair.left, pair.right);
  }

  /*
   * Template
   * 
   * Fields: this.left -- String this.right -- Integer
   * 
   * 
   * Methods:
   * 
   * this.renderLetterBlock() -- WorldImage this.renderLetter() -- WorldImage
   * this.renderBlock() -- WorldImage
   * 
   * 
   * 
   */

  // Renders this LetterBlock to an image
  WorldImage renderLetterBlock() {
    return new OverlayImage(this.renderLetter(), this.renderBlock());
  }

  // Renders the letter part of this to an image
  WorldImage renderLetter() {
    return new TextImage(this.left, TEXT_COLOR);
  }

  // Renders a block according to the width and length
  WorldImage renderBlock() {
    if (this.right < 0) {
      return new RectangleImage(BLOCK_WIDTH, BLOCK_HEIGHT, OutlineMode.SOLID,
          INCORRECT_LETTER_COLOR);
    }
    else if (this.right > 0) {
      return new RectangleImage(BLOCK_WIDTH, BLOCK_HEIGHT, OutlineMode.SOLID,
          MISPLACED_LETTER_COLOR);
    }
    else {
      return new RectangleImage(BLOCK_WIDTH, BLOCK_HEIGHT, OutlineMode.SOLID, CORRECT_LETTER_COLOR);
    }
  }

}

// Represents a function object turning a pair into a letter block
class PairToLetterBlock implements IFunc<Pair<String, Integer>, LetterBlock> {

  // Applies this function object to a Pair, returning a LetterBlock
  public LetterBlock apply(Pair<String, Integer> pair) {
    return new LetterBlock(pair.left, pair.right);
  }
}

// Represents a function object turning a LetterBlock to a string
class LetterBlockToString implements IFunc<LetterBlock, String> {

  // Applies this function object to a letter block, returning a String
  public String apply(LetterBlock lb) {
    return lb.left;
  }
}

// Represents a Game of Dordle
class Dordle extends World implements IRenderingConstants {
  Wordle wordleLeft; // Left side wordle game
  Wordle wordleRight; // Right side wordle game
  int additionalGuesses = 1; // Additional guesses on top of a default wordle

  /*
   * Template
   * 
   * Fields: this.wordleLeft -- Wordle this.wordleRight -- Wordle
   * this.additionalGuesses -- int
   * 
   * Methods:
   * 
   * Game Over Method: --this.isGameOver()-- -- boolean --this.shouldWorldEnd() --
   * boolean --this.win() -- boolean --this.lose() -- boolean
   * 
   * Rendering Methods: --this.makeScene()-- -- WorldScene --
   * this.lastScene(String msg) -- WorldScene
   * 
   * Key and Helper Methods: --this.onKeyEvent(String key)-- -- Dordle
   * 
   * 
   * 
   * Methods on fields:
   * 
   * wordleLeft.onKeyEvent(String key) -- Wordle wordleLeft.onCommitGuess() --
   * Wordle wordleLeft.onValidLetter(String l) -- Wordle
   * wordleLeft.onDeleteLetter() -- Wordle wordleLeft.shouldWorldEnd() -- boolean
   * wordleLeft.isGameOver() -- boolean wordleLeft.win() -- boolean
   * wordleLeft.lose() -- boolean wordleLeft.lost(int additionalGuesses) --
   * boolean wordleLeft.drawScene() -- WorldImage
   * wordleLeft.drawAllCommitedGuesses() -- WorldImage
   * wordleLeft.drawTypingBlocks() -- WorldImage wordleLeft.overlayText(WorldImage
   * base) -- WorldImage wordleLeft.lastScene(String msg) -- WorldScene
   * wordleLeft.makeScene() -- WorldScene
   * 
   * 
   * wordleRight.onKeyEvent(String key) -- Wordle wordleRight.onCommitGuess() --
   * Wordle wordleRight.onValidLetter(String l) -- Wordle
   * wordleRight.onDeleteLetter() -- Wordle wordleRight.shouldWorldEnd() --
   * boolean wordleRight.isGameOver() -- boolean wordleRight.win() -- boolean
   * wordleRight.lose() -- boolean wordleRight.lost(int additionalGuesses) --
   * boolean wordleRight.drawScene() -- WorldImage
   * wordleRight.drawAllCommitedGuesses() -- WorldImage
   * wordleRight.drawTypingBlocks() -- WorldImage
   * wordleRight.overlayText(WorldImage base) -- WorldImage
   * wordleRight.lastScene(String msg) -- WorldScene wordleRight.makeScene() --
   * WorldScene
   * 
   */

  Dordle(Wordle wordleLeft, Wordle wordleRight) {
    this.wordleLeft = wordleLeft;
    this.wordleRight = wordleRight;
  }

  public Dordle onKeyEvent(String key) {
    return new Dordle(this.wordleLeft.onKeyEvent(key), this.wordleRight.onKeyEvent(key));
  }

  // Should this world end?
  public boolean shouldWorldEnd() {
    // World should end when this game is over
    return isGameOver();
  }

  // Is the game over?
  boolean isGameOver() {
    // Game is over if it is won or lost
    return this.win() || this.lose();
  }

  // Did the player win?
  boolean win() {
    return wordleLeft.win() && wordleRight.win();
  }

  // Did the player lose?
  boolean lose() {
    return wordleLeft.lose(additionalGuesses) || wordleRight.lose(additionalGuesses);
  }

  public WorldScene lastScene(String msg) {
    WorldScene scene = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT);
    // Creating a spacer to seperate the two wordle games
    WorldImage spacer = new RectangleImage(BLOCK_WIDTH, (int) (SCREEN_HEIGHT / 2),
        OutlineMode.SOLID, BACKGROUND_COLOR);
    // Rendering two wordle games on either side of the spacer
    WorldImage worldImage = new BesideImage(this.wordleLeft.overlayText(wordleLeft.drawScene()),
        spacer, this.wordleRight.overlayText(wordleRight.drawScene()));

    return scene.placeImageXY(worldImage, (int) (SCREEN_WIDTH / 2), (int) (SCREEN_HEIGHT / 1.5));
  }

  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT);
    // Creating a spacer to seperate the two wordle games
    WorldImage spacer = new RectangleImage(BLOCK_WIDTH, (int) (SCREEN_HEIGHT / 2),
        OutlineMode.SOLID, BACKGROUND_COLOR);
    // Rendering two wordle games on either side of the spacer
    WorldImage worldImage = new BesideImage(wordleLeft.drawScene(), spacer,
        wordleRight.drawScene());
    return scene.placeImageXY(worldImage, (int) (SCREEN_WIDTH / 2), (int) (SCREEN_HEIGHT / 1.5));
  }

}

// Represents our world for the wordle word game 
class Wordle extends World implements IRenderingConstants {
  IList<String> alphabet = new ConsLo<String>("a",
      new ConsLo<String>("b",
          new ConsLo<String>("c",
              new ConsLo<String>("d", new ConsLo<String>("e",
                  new ConsLo<String>("f", new ConsLo<String>("g", new ConsLo<String>("h",
                      new ConsLo<String>("i", new ConsLo<String>("j", new ConsLo<String>("k",
                          new ConsLo<String>("l", new ConsLo<String>("m", new ConsLo<String>("n",
                              new ConsLo<String>("o", new ConsLo<String>("p",
                                  new ConsLo<String>("q", new ConsLo<String>("r",
                                      new ConsLo<String>("s", new ConsLo<String>("t",
                                          new ConsLo<String>("u", new ConsLo<String>("v",
                                              new ConsLo<String>("w", new ConsLo<String>("x",
                                                  new ConsLo<String>("y", new ConsLo<String>("z",
                                                      new MtLo<String>()))))))))))))))))))))))))));
  Random rand;
  IList<String> secretCode; // represents the secret code
  int numOfGuesses; // the number of guesses taken
  // We were thinking of adding a boolean gameOver but we aren't sure

  IList<IList<LetterBlock>> guessLetterBlocks; // a list containing letter blocks of the guesses

  // where 0 is a correct letter in the correct spot
  // where 1 is a correct letter in the incorrect spot
  // and -1 is a letter that isn't in the secretCode

  IList<String> wl; // Keeps track of the word list

  IList<String> typingText; // what is currently being typed by the user

  String secretWord; // Secret word! (in string format)

  Wordle(Random rand, int numOfGuesses, IList<IList<LetterBlock>> guessLetterBlocks,
      IList<String> typingText, IList<String> wl) {

    this.rand = rand;
    this.secretCode = new WordList()
        .convertWordToList(new WordList().split(new WordList().fiveLetters, 5, 1).getListItem(
            rand.nextInt(new WordList().split(new WordList().fiveLetters, 5, 1).len())));

    this.numOfGuesses = numOfGuesses;
    this.guessLetterBlocks = guessLetterBlocks;
    this.typingText = typingText;
    this.wl = wl;
    secretWord = this.secretCode.foldr(new ConcatenateString(), "");
  }

  Wordle(Random rand, IList<String> secretCode, int numOfGuesses,
      IList<IList<LetterBlock>> guessLetterBlocks, IList<String> typingText, IList<String> wl) {

    this.rand = rand;
    this.secretCode = secretCode;

    // this will be replaced later on just a placeholder until
    // we define our getListItem method that will return the
    // secretCode using the random number generated.

    this.numOfGuesses = numOfGuesses;
    this.guessLetterBlocks = guessLetterBlocks;
    this.typingText = typingText;
    this.wl = wl;
    secretWord = this.secretCode.foldr(new ConcatenateString(), "");
  }

  Wordle(int numOfGuesses, IList<IList<LetterBlock>> guessLetterBlocks, IList<String> typingText,
      IList<String> wl) {
    this(new Random(), numOfGuesses, guessLetterBlocks, typingText, wl);
  }

  Wordle(IList<String> secretCode, int numOfGuesses, IList<IList<LetterBlock>> guessLetterBlocks,
      IList<String> typingText, IList<String> wl) {
    this(new Random(), secretCode, numOfGuesses, guessLetterBlocks, typingText, wl);
  }

  /*
   * Template
   * 
   * Fields: this.rand -- Random this.secretCode -- IList<String>
   * this.numOfGuesses -- int this.guessLetterBlocks -- IList<IList<LetterBlock>>
   * this.wl -- IList<String> this.typingText -- IList<String> this.secretWord --
   * String
   * 
   * 
   * Methods:
   * 
   * this.onKeyEvent(String key) -- Wordle this.onCommitGuess() -- Wordle
   * this.onValidLetter(String l) -- Wordle this.onDeleteLetter() -- Wordle
   * this.shouldWorldEnd() -- boolean this.isGameOver() -- boolean this.win() --
   * boolean this.lose() -- boolean this.lost(int additionalGuesses) -- boolean
   * this.drawScene() -- WorldImage this.drawAllCommitedGuesses() -- WorldImage
   * this.drawTypingBlocks() -- WorldImage this.overlayText(WorldImage base) --
   * WorldImage this.lastScene(String msg) -- WorldScene this.makeScene() --
   * WorldScene
   * 
   * 
   * 
   * Methods on fields: rand.nextInt(int maxVal) -- int
   * 
   * secretCode.len() -- int secretCode.map(IFunc<String, R>) -- IList<R>
   * secretCode.getListItem(int n) -- String <R>
   * secretCode.rest.foldl(IFoldFunc<String, R> func, R current) -- R <R>
   * secretCode.foldlWithCounter(IFoldFuncWithCounter<String, R>, int, R) -- R <R>
   * secretCode.rest.foldr(IFoldFunc<String, R> func, R current) -- R
   * secretCode.sameList(IList<String> other) - boolean
   * secretCode.sameCons(ConsLo<String> other) - boolean
   * secretCode.sameMt(MtLo<String> other) - boolean secretCode.contains(String
   * item) - boolean secretCode.append(IList<String> other) - IList<String>
   * secretCode.reverse() - IList<String> secretCode.removeFirst() - IList<String>
   * 
   * 
   * guessLetterBlocks.len() -- int
   * guessLetterBlocks.map(IFunc<IList<LetterBlock>, R>) -- IList<R>
   * guessLetterBlocks.getListItem(int n) -- IList<LetterBlock> <R>
   * guessLetterBlocks.rest.foldl(IFoldFunc<IList<LetterBlock>, R> func, R
   * current) -- R <R>
   * guessLetterBlocks.foldlWithCounter(IFoldFuncWithCounter<IList<LetterBlock>,
   * R>, int, R)- R <R> guessLetterBlocks.rest.foldr(IFoldFunc<IList<LetterBlock>,
   * R> func, R current) -- R guessLetterBlocks.sameList(IList<IList<LetterBlock>
   * other) - boolean guessLetterBlocks.sameCons(ConsLo<IList<LetterBlock> other)
   * - boolean guessLetterBlocks.sameMt(MtLo<IList<LetterBlock>> other) - boolean
   * guessLetterBlocks.contains(IList<LetterBlock> item) - boolean
   * guessLetterBlocks.append(IList<IList<LetterBlock>> other) -
   * IList<IList<LetterBlock>> guessLetterBlocks.reverse() -
   * IList<IList<LetterBlock>> guessLetterBlocks.removeFirst() -
   * IList<IList<LetterBlock>
   * 
   * wl.len() -- int wl.map(IFunc<String, R>) -- IList<R> wl.getListItem(int n) --
   * String <R> wl.rest.foldl(IFoldFunc<String, R> func, R current) -- R <R>
   * wl.foldlWithCounter(IFoldFuncWithCounter<String, R>, int, R) -- R <R>
   * wl.rest.foldr(IFoldFunc<String, R> func, R current) -- R
   * wl.sameList(IList<String> other) - boolean wl.sameCons(ConsLo<String> other)
   * - boolean wl.sameMt(MtLo<String> other) - boolean wl.contains(String item) -
   * boolean wl.append(IList<String> other) - IList<String> wl.reverse() -
   * IList<String> wl.removeFirst() - IList<String>
   * 
   * typingText.len() -- int typingText.map(IFunc<String, R>) -- IList<R>
   * typingText.getListItem(int n) -- String <R>
   * typingText.rest.foldl(IFoldFunc<String, R> func, R current) -- R <R>
   * typingText.foldlWithCounter(IFoldFuncWithCounter<String, R>, int, R) -- R <R>
   * typingText.rest.foldr(IFoldFunc<String, R> func, R current) -- R
   * typingText.sameList(IList<String> other) - boolean
   * typingText.sameCons(ConsLo<String> other) - boolean
   * typingText.sameMt(MtLo<String> other) - boolean typingText.contains(String
   * item) - boolean typingText.append(IList<String> other) - IList<String>
   * typingText.reverse() - IList<String> typingText.removeFirst() - IList<String>
   * 
   * 
   * 
   */

  // Handles user input and modifies wordle world accordingly
  public Wordle onKeyEvent(String key) {
    // If this game is won, don't change the world state
    if (this.win()) {
      return this;
    }
    else {
      if (key.equals("enter")) {
        return this.onCommitGuess();
      }
      else if (key.equals("backspace")) {
        return this.onDeleteLetter();
      }
      else {
        return this.onValidLetter(key);
      }
    }
  }

  // Checks if a guess is valid and then commits it

  // Runs when a user commits a guess
  Wordle onCommitGuess() {

    // Guess is incorrect length, thus don't let them commit and return current
    // world
    if (this.typingText.len() < this.secretCode.len()) {
      return this;
    }
    else {
      // Guess is correct length, testing whether its a valid word in the word list
      String currentGuess = this.typingText.foldr(new ConcatenateString(), "");
      if (this.wl.contains(currentGuess)) {
        IList<LetterBlock> guessLetterBlock = this.typingText
            .foldlWithCounter(new CompareGuess(this.secretWord), 0, new MtLo<LetterBlock>())
            .reverse();

        // To add to back, reverses and adds to front and then will reverse again when
        // creating new nordle.
        IList<IList<LetterBlock>> newBoardReversed = new ConsLo<IList<LetterBlock>>(
            guessLetterBlock, this.guessLetterBlocks.reverse());

        return new Wordle(this.rand, this.secretCode, this.numOfGuesses + 1,
            newBoardReversed.reverse(), new MtLo<String>(), this.wl);
      }
      else {
        return new Wordle(this.rand, this.secretCode, this.numOfGuesses, this.guessLetterBlocks,
            new MtLo<String>(), this.wl);
      }

    }
  }

  // Checks if the key the user typed is a letter or not

  // Runs when a user enters a valid letter
  Wordle onValidLetter(String l) {

    if (this.typingText.len() == this.secretCode.len()) {
      return this;
    }
    else if (this.alphabet.contains(l)) {
      return new Wordle(this.rand, this.secretCode, this.numOfGuesses, this.guessLetterBlocks,
          this.typingText.append(new ConsLo<String>(l.toUpperCase(), new MtLo<String>())), this.wl);
    }
    else {
      return this;
    }
  }

  // Deletes the last letter the user typed

  // Runs when a user hits delete
  Wordle onDeleteLetter() {

    if (this.typingText.len() == 0) {
      return this;
    }
    else {
      // Removes last element from the list (reverses, removes first, and reverses)
      IList<String> newType = this.typingText.reverse().removeFirst().reverse();

      // Returns the world with this new typing text
      return new Wordle(this.rand, this.secretCode, this.numOfGuesses, this.guessLetterBlocks,
          newType, this.wl);
    }
  }

  // Should the world end? (very morbid question)

  // Should this world end?
  public boolean shouldWorldEnd() {

    // World should end when this game is over
    return isGameOver();
  }

  // is the same over?

  // Is the game over?
  boolean isGameOver() {

    // Game is over if it is won or lost
    return this.win() || this.lose();
  }

  // Did the user win?

  // Did the player win?
  boolean win() {
    if (this.numOfGuesses <= 6 && this.guessLetterBlocks.len() > 0) {
      return this.guessLetterBlocks.getListItem(this.guessLetterBlocks.len() - 1)
          .foldl(new AllOneLBFoldFunc(), true);
    }
    else {
      return false;
    }
  }

  // Did the user lose?

  // Did the player lose?
  boolean lose() {

    return !this.win() && this.numOfGuesses == 6;
  }

  // Did the user lose when taking into account additional guesses?

  // Did the player lose given they have extra guesses?
  boolean lose(int additionalGuesses) {

    return !this.win() && this.numOfGuesses == (6 + additionalGuesses);
  }

  // RENDERING STUFF:
  // draws out the foundation of the world
  WorldImage drawScene() {
    return new AboveImage(this.drawAllCommitedGuesses(), this.drawTypingBlocks());
    // Draws guesses above
    // Typing blocks
  }

  // Draws the committed guesses
  WorldImage drawAllCommitedGuesses() {
    // Fields:
    //// this.guessLetterBlocks - IList<IList<LetterBlock>> ; // a list containing
    // letter blocks of the guesses
    return this.guessLetterBlocks.foldl(new AboveSingleGuessFunc(), new EmptyImage());
  }

  // Draws current user typing to typing blocks
  WorldImage drawTypingBlocks() {
    IList<TypingBlock> typingBlocks = this.typingText.map(new StringToTypingBlock());
    IList<WorldImage> typingBlockImageList = typingBlocks.map(new TypingBlockToImage());

    // Fields: this.typingtext - IList<String>
    return typingBlockImageList.foldl(new BesideFoldFunc(), new EmptyImage());
  }

  // Overlays the winning or losing message
  public WorldImage overlayText(WorldImage base) {
    String display = "YOU LOSE :(, WORD WAS: " + this.secretWord;
    if (this.win()) {
      display = "YOU WON!!";
    }

    WorldImage textImage = new TextImage(display, Color.RED);
    return new AboveImage(textImage, base);
  }

  // Draws the last scene of the game
  public WorldScene lastScene(String msg) {
    WorldScene s = new WorldScene(700, 700);
    return s.placeImageXY(this.overlayText(this.drawScene()), 350, 500);
  }

  // Makes the scene of the game

  // Default render method of bigbang
  public WorldScene makeScene() {
    WorldScene s = new WorldScene(SCREEN_HEIGHT, SCREEN_WIDTH);
    return s.placeImageXY(this.drawScene(), (int) (SCREEN_HEIGHT / 2), (int) (SCREEN_WIDTH / 1.3));
  }

}

class WordList {
  String fiveLetters = "WHICH,THEIR,WOULD,THERE,COULD,ABOUT,OTHER,THESE,FIRST,AFTER,THINK,"
      + "YEARS,THOSE,BEING,THREE,THERE,STILL,WHERE,MIGHT,WORLD,AGAIN,NEVER,"
      + "UNDER,WHILE,HOUSE,WHERE,ABOUT,LOCAL,PLACE,GREAT,SMALL,GROUP,QUITE,"
      + "PARTY,EVERY,WOMEN,OFTEN,GIVEN,MONEY,POINT,NIGHT,STATE,TAKEN,RIGHT,"
      + "THING,WATER,RIGHT,LARGE,ASKED,POWER,LATER,YOUNG,SINCE,TIMES,COURT,"
      + "DOING,EARLY,TODAY,USING,WORDS,CHILD,UNTIL,LEVEL,KNOWN,MAJOR,BEGAN,"
      + "AREAS,AFTER,WOMAN,AMONG,CLEAR,STAFF,BLACK,WHOLE,SENSE,SEEMS,THIRD,"
      + "WHITE,DEATH,SHALL,HEARD,TABLE,WHOSE,ORDER,RANGE,STUDY,TRADE,HOURS,"
      + "HANDS,BASED,LEAVE,HUMAN,CASES,CLASS,VOICE,SINCE,SHORT,VALUE,PAPER,"
      + "SEVEN,EIGHT,PRICE,RIGHT,UNTIL,MAKES,UNION,TERMS,SOUTH,NORTH,STAGE,"
      + "COMES,BRING,WEEKS,START,SHOWN,MUSIC,MONTH,TRIED,WRONG,ISSUE,AWARD,"
      + "ROYAL,MOVED,LIGHT,BASIS,FIELD,ADDED,MEANS,ROUND,HEART,ABOVE,STORY,"
      + "FORCE,BOARD,STOOD,BOOKS,LEGAL,MODEL,BUILT,FINAL,CLOSE,SPACE,ALONG,"
      + "TOTAL,THANK,PRIME,COSTS,TAKES,HAPPY,PARTS,SPENT,FLOOR,ROUND,ALLOW,"
      + "RATES,SORRY,HOTEL,MEANT,LOWER,IDEAS,BASIC,WRITE,AWARE,STYLE,RULES,"
      + "NEEDS,MAYBE,GIVES,SALES,EVENT,SOUND,READY,LINES,LOOKS,WORTH,PRESS,"
      + "BLOOD,GOODS,CARRY,WROTE,GREEN,SHOWS,OFFER,FORMS,MILES,NEEDS,PLANS,"
      + "EARTH,TITLE,GIRLS,MEANS,GLASS,HEAVY,SPEAK,RIVER,ABOVE,MOUTH,PIECE,"
      + "STAND,EXTRA,WHOLE,OLDER,FULLY,PEACE,WANTS,TYPES,BELOW,RADIO,CIVIL,"
      + "FIFTY,START,KNOWS,TREES,LEARN,TRUTH,WORKS,LIVED,SHARE,AGREE,FRONT,"
      + "MEDIA,AVOID,STONE,APPLY,LIVES,LATER,CHAIR,HORSE,QUEEN,NAMES,CELLS,"
      + "EARLY,VISIT,STOCK,CHIEF,DRAWN,FIRMS,BEGIN,IMAGE,VIEWS,SCALE,PLANT,"
      + "SPEND,VOICE,ALONE,TRUST,ENDED,CAUSE,CRIME,UNITS,SPEED,ALONG,SPOKE,"
      + "STUFF,FRONT,MATCH,BUILD,REACH,FRESH,SCENE,ITEMS,PHONE,STEPS,WATCH,"
      + "FORTY,SIGHT,BANKS,CLAIM,ENJOY,USERS,VIDEO,WORSE,TRAIN,TRIAL,JOINT,"
      + "DOUBT,COVER,USUAL,SMILE,SIDES,WHILE,WORKS,AHEAD,RURAL,TWICE,GAMES,"
      + "FUNDS,SHAPE,LIGHT,QUIET,POUND,RAISE,OUGHT,NOTED,EQUAL,HOMES,WALLS,"
      + "TALKS,OFFER,CAUSE,BREAK,SITES,QUICK,PROVE,NOTES,TRACK,BIRDS,ROUTE,"
      + "LIKED,OCCUR,UNDER,ROOMS,DAILY,BELOW,EXIST,CHECK,ALONE,URBAN,YOUTH,"
      + "EMPTY,LUNCH,UPPER,SHARE,DRUGS,SERVE,ENTER,WASTE,FACTS,SHOOK,FAITH,"
      + "SHOPS,MORAL,HEADS,BIRTH,BROKE,ENTRY,CROWN,VITAL,HOPED,TOTAL,VISIT,"
      + "TESTS,OWNER,WIDER,BROAD,DRINK,CLEAN,DOORS,HENCE,TEETH,BRAIN,BRIEF,"
      + "SIGNS,COVER,CLAIM,GOALS,GUIDE,DRIVE,IDEAL,BOUND,KINDS,WORRY,MINOR,"
      + "SEATS,NOISE,THICK,LOVED,METAL,GRAND,PHASE,COAST,LYING,WORST,ADULT,"
      + "FACED,INDEX,SPORT,JUDGE,BROWN,FUNNY,INNER,LEAST,PAGES,SHARP,DRIVE,"
      + "NAMED,SIXTY,AGENT,BADLY,PLACE,CROSS,GROWN,CROWD,ARGUE,CATCH,TEARS,"
      + "ALIVE,BEGUN,YOURS,ANGRY,SHEET,MOTOR,SHOCK,CLOSE,DRESS,GRASS,FRUIT,"
      + "TOWNS,LUCKY,TOUCH,PLATE,TIRED,FIGHT,SLEEP,TEAMS,STARS,CHEAP,CARDS,"
      + "ROADS,GRANT,THEME,ERROR,DREAM,HELLO,CHEST,REFER,BEACH,FOCUS,CLUBS,"
      + "BREAD,ADMIT,CHIEF,STEEL,LEADS,WAGES,TASKS,PANEL,YARDS,CHAIN,TELLS,"
      + "ARMED,SLEEP,SHOES,DROVE,FALSE,SUGAR,BLOCK,ASIDE,STORE,BREAK,RAPID,"
      + "FIXED,AIMED,OWNED,DRAMA,UNCLE,FIFTH,LINKS,SOLID,APART,SKILL,CLOSE,"
      + "DEALT,FILMS,ROUND,TASTE,PLANE,SCOPE,FAULT,ENEMY,ROUGH,LIMIT,ABUSE,"
      + "TOWER,ANGER,SWEET,ARISE,POINT,FACES,FEELS,COSTS,INPUT,TOUGH,SAVED,"
      + "TRULY,DRINK,TURNS,TOOLS,CYCLE,NURSE,FRAME,PROUD,PILOT,CALLS,GIVEN,"
      + "VOTES,CREAM,FEWER,THROW,AWFUL,THREW,HILLS,PRIZE,NOVEL,DEPTH,CALLS,"
      + "BILLS,REPLY,TREAT,GREEN,SHEEP,STUDY,CRIED,SOUND,DANCE,SORTS,PRESS,"
      + "FILES,FIGHT,STILL,ROCKS,PLAIN,WATCH,SINCE,FINDS,RATIO,COACH,FEARS,"
      + "SMOKE,RUGBY,SONGS,CLOCK,FIXED,HELPS,CHOSE,MINDS,MARKS,TRUST,STEAM,"
      + "SILLY,TEACH,UNITY,TAXES,SHIRT,ROUND,FINAL,BEING,ROLES,SCORE,LOANS,"
      + "DOZEN,PRIDE,NEWLY,BUYER,MATCH,SHIPS,WAVES,KNIFE,PROOF,MARRY,LIVES,"
      + "PITCH,BOXES,HOLES,ABOVE,APPLE,DIRTY,HOLDS,TREND,LOOSE,STATE,BLIND,"
      + "KNEES,BOOTS,SMELL,MUMMY,KEEPS,WHEEL,TOUCH,SHIFT,DRAFT,SQUAD,FLESH,"
      + "SPLIT,SIXTH,LEVEL,ROOTS,STICK,LAYER,RISKS,CURVE,ADOPT,GUARD,OUTER,"
      + "TOPIC,TENDS,HOPES,ANGLE,GUESS,WINGS,CLEAR,MEALS,RULED,PLAYS,TEXTS,"
      + "TIGHT,OPERA,PUPIL,BLAME,MIXED,GUEST,LOGIC,ACUTE,VOTED,ACTED,DELAY,"
      + "VALID,HABIT,BONES,CROSS,URGED,STORM,GROSS,STUCK,MALES,PAINT,POSTS,"
      + "EXACT,DADDY,AUDIT,LISTS,ALBUM,LEAVE,FORCE,FALLS,CANAL,FOCUS,MAYOR,"
      + "WORTH,COUNT,DOUBT,CRASH,PRINT,LAUGH,PAIRS,LEASE,GAINS,FOODS,ALARM,"
      + "SHEER,COUNT,CLOUD,GENES,LIKES,LANDS,SWEPT,NAKED,ASSET,BANDS,ACTOR,"
      + "CRAFT,PLANS,DIARY,OCEAN,BENCH,MIXED,BOATS,JUDGE,KNOWN,BIBLE,MOVES,"
      + "FIRED,CLOTH,SHELL,PIANO,CLERK,GATES,BONDS,WIVES,SOLVE,SADLY,SPARE,"
      + "GRADE,STAKE,ASIAN,CHEEK,ALTER,SHAME,DATES,ABBEY,FLEET,STAND,FLATS,"
      + "DEBTS,BURST,STICK,FAILS,LOCAL,CABLE,CHECK,CHIPS,ORDER,BRICK,GIANT,"
      + "HOPES,FARMS,GRAIN,FRAUD,SWUNG,NASTY,MOVIE,SHOWS,FORUM,RELAX,CRAZY,"
      + "SHOTS,REIGN,GUILT,LOVER,SLEPT,UPSET,FORMS,MOUSE,SIZES,VILLA,EDGES,"
      + "PANIC,LABEL,THEFT,RISEN,DEVIL,GIFTS,DYING,MAGIC,BRAVE,LAUGH,OPENS,"
      + "EATEN,GLORY,FENCE,JUICE,HATED,LIVER,SEEDS,MOVES,CHAOS,RANKS,ISSUE,"
      + "CLEAN,TRAIN,POEMS,DRUNK,PAUSE,STRIP,SUPER,ACRES,ESSAY,AROSE,PATCH,"
      + "CROPS,LIMIT,RACES,CLIMB,WIDOW,STEEP,DEBUT,CHART,WOODS,GRACE,BASES,"
      + "HARSH,LORDS,FIBRE,BRASS,BALLS,FAINT,ROSES,FLUID,SEEKS,VAGUE,VIRUS,"
      + "SHIFT,NAVAL,SHOOT,KINGS,WAVED,ADDED,MAGIC,SWORD,IMPLY,BLANK,SMART,"
      + "TANKS,TRIES,BUSES,SHORE,LOADS,STIFF,CITED,RIGID,TRICK,MINES,DRANK,"
      + "TAPES,EAGER,SKIRT,GRIEF,PARKS,PHONE,SHELF,WAIST,WASTE,SAUCE,COINS,"
      + "DANCE,WINDS,MEETS,BORED,BONUS,DAILY,CRUEL,FACES,VERSE,GHOST,SHADE,"
      + "FATAL,SLOPE,ANGEL,STRAW,UPSET,RIVAL,LOYAL,PATHS,SCORE,NOBLE,NAILS,"
      + "LORRY,BRAND,LOOKS,ORGAN,CARED,MANOR,CRUDE,BEANS,BRUSH,SPELL,DATED,"
      + "NERVE,PENCE,SERUM,AWAKE,BLOKE,FORTH,MINUS,RIDGE,POSED,PAINT,GROWS,"
      + "SUITE,REACH,OZONE,REACT,DEALS,JEANS,TALES,RALLY,GRANT,STUCK,EAGLE,"
      + "CHARM,GRAVE,CODES,REPLY,HUMAN,SOLAR,POLES,SHAKE,BLACK,BOWEL,PHOTO,"
      + "SPOTS,KNOCK,BLUES,SOUND,LOVES,PRIOR,BREED,GUIDE,MODES,BUNCH,FIRES,"
      + "STOPS,TOXIC,LEMON,BASIN,RINGS,SWING,FLOOD,TRAIL,LAKES,FETCH,BOMBS,"
      + "LINED,PENNY,WALKS,VENUE,DEALS,BLOWN,TILES,FANCY,CRACK,HEELS,TRUCK,"
      + "PLAYS,ALIKE,SMELL,WIPED,TRACE,USAGE,CORPS,ZONES,BACKS,PIPES,WIDTH,"
      + "WHITE,SMOKE,CAMPS,GAZED,SALAD,ARRAY,MAJOR,PLAIN,TENTH,SKULL,JOKES,"
      + "POOLS,TWINS,BORNE,YIELD,THUMB,DYING,CLASH,ARMED,WOUND,CABIN,MEDAL,"
      + "TRIPS,MERCY,BLADE,DRAWS,STAMP,FERRY,ALPHA,FLOWN,ELBOW,CLIFF,NOVEL,"
      + "SWEAT,PAINS,HONEY,WEIRD,TUTOR,PORTS,FLUNG,FEVER,TIGHT,WINES,SMILE,"
      + "FINED,MARCH,POLLS,LIMBS,MOUNT,TRACE,PULSE,WRIST,ATOMS,BRIDE,REALM,"
      + "CREWS,FLAME,FLOUR,PRINT,BOOST,LASER,YACHT,ARROW,VIVID,NOISY,QUOTE,"
      + "GRAPH,BOOST,BURNT,CEASE,SHOUT,CHOIR,ACIDS,MAKER,TOURS,SPARE,ADAPT,"
      + "CIVIC,BELLS,ALTOS,STEAL,CANOE,CRANE,CAVES,CHIMP,CHORE,COUGH,RAILS,"
      + "SAILS,SCALD,SCOLD,SCARE,TARTS,GLEAN";

  String fourLetterWords = "ABLE,ACID,AGED,ALSO,AREA,ARMY,AWAY,BABY,BACK,BALL,BAND,BANK,BASE,"
      + "BATH,BEAR,BEAT,BEEN,BEER,BELL,BELT,BEST,BILL,BIRD,BLOW,BLUE,BOAT,"
      + "BODY,BOMB,BOND,BONE,BOOK,BOOM,BORN,BOSS,BOTH,BOWL,BULK,BURN,BUSH,"
      + "BUSY,CALL,CALM,CAME,CAMP,CARD,CARE,CASE,CASH,CAST,CELL,CHAT,CHIP,"
      + "CITY,CLUB,COAL,COAT,CODE,COLD,COME,COOK,COOL,COPE,COPY,CORE,COST,"
      + "CREW,CROP,DARK,DATA,DATE,DAWN,DAYS,DEAD,DEAL,DEAN,DEAR,DEBT,DEEP,"
      + "DENY,DESK,DIAL,DIET,DIRT,DISC,DISK,DOES,DONE,DOOR,DOSE,DOWN,DRAW,"
      + "DREW,DROP,DRUG,DUAL,DUKE,DUST,DUTY,EACH,EARN,EASE,EAST,EASY,EDGE,"
      + "ELSE,EVEN,EVER,EVIL,EXIT,FACE,FACT,FAIL,FAIR,FALL,FARM,FAST,FATE,"
      + "FEAR,FEED,FEEL,FEET,FELL,FELT,FILE,FILL,FILM,FIND,FINE,FIRE,FIRM,"
      + "FISH,FIVE,FLAT,FLOW,FOOD,FOOT,FORD,FORM,FORT,FOUR,FREE,FROM,FUEL,"
      + "FULL,FUND,GAIN,GAME,GATE,GAVE,GEAR,GENE,GIFT,GIRL,GIVE,GLAD,GOAL,"
      + "GOES,GOLD,GOLF,GONE,GOOD,GRAY,GREW,GREY,GROW,GULF,HAIR,HALF,HALL,"
      + "HAND,HANG,HARD,HARM,HATE,HAVE,HEAD,HEAR,HEAT,HELD,HELL,HELP,HERE,"
      + "HERO,HIGH,HILL,HIRE,HOLD,HOLE,HOLY,HOME,HOPE,HOST,HOUR,HUGE,HUNG,"
      + "HUNT,HURT,IDEA,INCH,INTO,IRON,ITEM,JACK,JANE,JEAN,JOHN,JOIN,JUMP,"
      + "JURY,JUST,KEEN,KEEP,KENT,KEPT,KICK,KILL,KIND,KING,KNEE,KNEW,KNOW,"
      + "LACK,LADY,LAID,LAKE,LAND,LANE,LAST,LATE,LEAD,LEFT,LESS,LIFE,LIFT,"
      + "LIKE,LINE,LINK,LIST,LIVE,LOAD,LOAN,LOCK,LOGO,LONG,LOOK,LORD,LOSE,"
      + "LOSS,LOST,LOVE,LUCK,MADE,MAIL,MAIN,MAKE,MALE,MANY,MARK,MASS,MATT,"
      + "MEAL,MEAN,MEAT,MEET,MENU,MERE,MIKE,MILE,MILK,MILL,MIND,MINE,MISS,"
      + "MODE,MOOD,MOON,MORE,MOST,MOVE,MUCH,MUST,NAME,NAVY,NEAR,NECK,NEED,"
      + "NEWS,NEXT,NICE,NICK,NINE,NONE,NOSE,NOTE,OKAY,ONCE,ONLY,ONTO,OPEN,"
      + "ORAL,OVER,PACE,PACK,PAGE,PAID,PAIN,PAIR,PALM,PARK,PART,PASS,PAST,"
      + "PATH,PEAK,PICK,PINK,PIPE,PLAN,PLAY,PLOT,PLUG,PLUS,POLL,POOL,POOR,"
      + "PORT,POST,PULL,PURE,PUSH,RACE,RAIL,RAIN,RANK,RARE,RATE,READ,REAL,"
      + "REAR,RELY,RENT,REST,RICE,RICH,RIDE,RING,RISE,RISK,ROAD,ROCK,ROLE,"
      + "ROLL,ROOF,ROOM,ROOT,ROSE,RULE,RUSH,RUTH,SAFE,SAID,SAKE,SALE,SALT,"
      + "SAME,SAND,SAVE,SEAT,SEED,SEEK,SEEM,SEEN,SELF,SELL,SEND,SENT,SEPT,"
      + "SHIP,SHOP,SHOT,SHOW,SHUT,SICK,SIDE,SIGN,SITE,SIZE,SKIN,SLIP,SLOW,"
      + "SNOW,SOFT,SOIL,SOLD,SOLE,SOME,SONG,SOON,SORT,SOUL,SPOT,STAR,STAY,"
      + "STEP,STOP,SUCH,SUIT,SURE,TAKE,TALE,TALK,TALL,TANK,TAPE,TASK,TEAM,"
      + "TECH,TELL,TEND,TERM,TEST,TEXT,THAN,THAT,THEM,THEN,THEY,THIN,THIS,"
      + "THUS,TILL,TIME,TINY,TOLD,TOLL,TONE,TONY,TOOK,TOOL,TOUR,TOWN,TREE,"
      + "TRIP,TRUE,TUNE,TURN,TWIN,TYPE,UNIT,UPON,USED,USER,VARY,VAST,VERY,"
      + "VICE,VIEW,VOTE,WAGE,WAIT,WAKE,WALK,WALL,WANT,WARD,WARM,WASH,WAVE,"
      + "WAYS,WEAK,WEAR,WEEK,WELL,WENT,WERE,WEST,WHAT,WHEN,WHOM,WIDE,WIFE,"
      + "WILD,WILL,WIND,WINE,WING,WIRE,WISE,WISH,WITH,WOOD,WORD,WORE,WORK,"
      + "YARD,YEAH,YEAR,YOUR,ZERO,ZONE,CAVE";

  /*
   * Template
   * 
   * Fields: --this.fiveLetters-- -- String --this.fourLetterWords-- -- String
   * 
   * Methods: --this.split(String string, int chunkLength, int sepLength)--
   * --IList<String>
   * 
   * 
   */

  // Splits a given string into chunks of a given positive length,
  // that are separated by gaps of a given non-negative length.
  IList<String> split(String string, int chunkLength, int sepLength) {
    if (string.length() < chunkLength) {
      return new MtLo<String>();
    }
    else if (string.length() == chunkLength) {
      return new ConsLo<String>(string, new MtLo<String>());
    }
    else {
      return new ConsLo<String>(string.substring(0, chunkLength),
          this.split(string.substring(chunkLength + sepLength), chunkLength, sepLength));
    }
  }

  // converts the given word into a list of strings;
  IList<String> convertWordToList(String word) {
    if (word.length() == 0) {
      return new MtLo<String>();
    }
    else if (word.length() == 1) {
      return new ConsLo<String>(word, new MtLo<String>());
    }
    else {
      return new ConsLo<String>(word.substring(0, 1), this.convertWordToList(word.substring(1)));
    }
  }

}

class ExamplesStarter implements IRenderingConstants {
  WordList wl = new WordList();
  IList<String> mtLString = new MtLo<String>();
  IList<Integer> mtLInt = new MtLo<Integer>();
  IList<String> l1String = new ConsLo<String>("hello", new ConsLo<String>("world", mtLString));
  String toBeSeperated = "hello/world"; // 1 sep length
  String toBeSeperated2 = "hello2dworld"; // 2 sep length
  String toBeSeperated3 = "hello2dworldcut"; // 2 sep length + cutoff

  <T> ConsLo<T> makeList(T one, T two, T three, T four, T five) {
    return new ConsLo<T>(one, new ConsLo<T>(two,
        new ConsLo<T>(three, new ConsLo<T>(four, new ConsLo<T>(five, new MtLo<T>())))));
  }

  IList<IList<String>> noGuesses = new MtLo<IList<String>>();

  // Guess of hello, a list of "h" "e" "l" "l" "o"
  IList<String> guessOfHello = wl.convertWordToList("HELLO");
  IList<LetterBlock> helloGuess = makeList(new LetterBlock("H", -1), new LetterBlock("E", 1),
      new LetterBlock("L", -1), new LetterBlock("L", -1), new LetterBlock("O", 1));
  // comparing "HELLO" to "OCEAN"

  // Guess of "AHHHHH"
  IList<String> guessOfAAAAH = wl.convertWordToList("AAAAH");
  // won't be accepted

  // Guess of world, a list of "W" "O" "R" "L" "D"
  IList<String> guessOfWorld = wl.convertWordToList("WORLD");
  IList<LetterBlock> worldGuess = makeList(new LetterBlock("W", -1), new LetterBlock("O", 1),
      new LetterBlock("R", -1), new LetterBlock("L", -1), new LetterBlock("D", -1));
  // comparing "WORLD" to "OCEAN"

  // Guess of OUTER
  IList<String> guessOfOuter = wl.convertWordToList("OUTER");
  IList<LetterBlock> outerGuess = makeList(new LetterBlock("O", 0), new LetterBlock("U", -1),
      new LetterBlock("T", -1), new LetterBlock("E", 1), new LetterBlock("R", -1));
  // comparing "OUTER" to "OCEAN"

  // Guess of BIRDS
  IList<String> guessOfBirds = wl.convertWordToList("BIRDS");
  IList<LetterBlock> birdsGuess = makeList(new LetterBlock("B", -1), new LetterBlock("I", -1),
      new LetterBlock("R", -1), new LetterBlock("D", -1), new LetterBlock("S", -1));
  // comparing "BIRDS" to "OCEAN"

  // Guess of QUEEN
  IList<String> guessOfQueen = wl.convertWordToList("QUEEN");
  IList<LetterBlock> queenGuess = makeList(new LetterBlock("Q", -1), new LetterBlock("U", -1),
      new LetterBlock("E", 0), new LetterBlock("E", 1), new LetterBlock("N", 0));
  // comparing "QUEEN" to "OCEAN"

  // Guess of CANOE
  IList<String> guessOfCanoe = wl.convertWordToList("CANOE");
  IList<LetterBlock> canoeGuess = makeList(new LetterBlock("C", 1), new LetterBlock("A", 1),
      new LetterBlock("N", 1), new LetterBlock("O", 1), new LetterBlock("E", 1));
  // comparing "CANOE" to "OCEAN"

  // Example of secretWord (OCEAN)
  IList<String> secretWord = wl.convertWordToList("OCEAN");
  IList<LetterBlock> oceanGuess = makeList(new LetterBlock("O", 0), new LetterBlock("C", 0),
      new LetterBlock("E", 0), new LetterBlock("A", 0), new LetterBlock("N", 0));
  // comparing "OCEAN" to "OCEAN"

  // Guess of GLEAN
  IList<String> guessOfGlean = wl.convertWordToList("GLEAN");
  IList<LetterBlock> gleanGuess = makeList(new LetterBlock("G", -1), new LetterBlock("L", -1),
      new LetterBlock("E", 0), new LetterBlock("A", 0), new LetterBlock("N", 0));
  // comparing "GLEAN" to "OCEAN"

  // Guess of WALLS
  IList<String> guessOfWalls = wl.convertWordToList("WALLS");
  IList<LetterBlock> wallsGuess = makeList(new LetterBlock("W", 0), new LetterBlock("A", 0),
      new LetterBlock("L", 0), new LetterBlock("L", 0), new LetterBlock("S", 0));
  // comparing "WALLS" to "WALLS"

  // Guess of ERROR
  IList<String> guessOfError = wl.convertWordToList("ERROR");
  IList<LetterBlock> errorGuess = makeList(new LetterBlock("E", -1), new LetterBlock("R", -1),
      new LetterBlock("R", -1), new LetterBlock("O", -1), new LetterBlock("R", -1));
  // comparing "ERROR" to "WALLS"

  // Guess of TARTS
  IList<String> guessOfTarts = wl.convertWordToList("TARTS");
  IList<LetterBlock> tartsGuess = makeList(new LetterBlock("T", 1), new LetterBlock("A", 1),
      new LetterBlock("R", 1), new LetterBlock("T", 1), new LetterBlock("S", 1));
  // comparing "TARTS" to "START"

  // Guess of BOOKS
  IList<String> guessOfBooks = wl.convertWordToList("BOOKS");
  IList<LetterBlock> booksGuess = makeList(new LetterBlock("B", -1), new LetterBlock("O", 0),
      new LetterBlock("O", 0), new LetterBlock("K", -1), new LetterBlock("S", 0));
  // comparing "BOOKS" to "GOODS"

  // Guess of THERE
  IList<String> guessOfThere = wl.convertWordToList("THERE");
  IList<LetterBlock> thereGuess = makeList(new LetterBlock("T", -1), new LetterBlock("H", -1),
      new LetterBlock("E", 1), new LetterBlock("R", 1), new LetterBlock("E", 1));
  // comparing "THERE" to "EARED"

  // Guess of BLOOD
  IList<String> guessOfBlood = wl.convertWordToList("BLOOD");
  IList<LetterBlock> bloodGuess = makeList(new LetterBlock("B", -1), new LetterBlock("L", -1),
      new LetterBlock("O", 0), new LetterBlock("O", 1), new LetterBlock("D", 1));
  // comparing "BLOOD" to "GOODS"

  // Guess of ERROR
  IList<String> guessOfLoan = wl.convertWordToList("LOAN");
  IList<LetterBlock> loanGuess = new ConsLo<LetterBlock>(new LetterBlock("L", 0),
      new ConsLo<LetterBlock>(new LetterBlock("O", 0),
          new ConsLo<LetterBlock>(new LetterBlock("A", 0),
              new ConsLo<LetterBlock>(new LetterBlock("N", -1), new MtLo<LetterBlock>()))));
  // comparing "LOAN" to "LOAD"

  /*
  
  
  // Test the split method
  boolean testSplit(Tester t) {
    return t.checkExpect(wl.split(toBeSeperated, 5, 1), l1String)
        && t.checkExpect(wl.split(toBeSeperated2, 5, 2), l1String)
        && t.checkExpect(wl.split(toBeSeperated3, 5, 2), l1String);
  }

  // Word list of length 5
  IList<String> wordListLength5 = wl.split(wl.fiveLetters, 5, 1);
  // Word list of length 4
  IList<String> wordListLength4 = wl.split(wl.fourLetterWords, 4, 1);

  IList<IList<LetterBlock>> emptyLetterBlock = new MtLo<IList<LetterBlock>>();

  IList<LetterBlock> queenOnOcean = new ConsLo<LetterBlock>(new LetterBlock("Q", -1),
      new ConsLo<LetterBlock>(new LetterBlock("U", -1),
          new ConsLo<LetterBlock>(new LetterBlock("E", 0),
              new ConsLo<LetterBlock>(new LetterBlock("E", 1),
                  new ConsLo<LetterBlock>(new LetterBlock("N", 0), new MtLo<LetterBlock>())))));
  IList<IList<LetterBlock>> guessQueenOnly = new ConsLo<IList<LetterBlock>>(queenOnOcean,
      emptyLetterBlock);

  // tests the len method
  boolean testLen(Tester t) {
    return t.checkExpect(new MtLo<Integer>().len(), 0)
        && t.checkExpect(makeList(11, 92, 37, 48, 53).len(), 5);
  }

  // function that converts string to integer based on length of string
  class StringToInt implements IFunc<String, Integer> {
    public Integer apply(String str) {
      return str.length();
    }
  }

  // tests the map method
  boolean testMap(Tester t) {
    return t.checkExpect(new MtLo<String>().map(new StringToInt()), new MtLo<Integer>())
        && t.checkExpect(
            makeList("hello", "why", "at", "angrier", "imagination").map(new StringToInt()),
            makeList(5, 3, 2, 7, 11));
  }

  // tests the get list item method
  boolean testGetItem(Tester t) {
    return t.checkException(
        new RuntimeException("Tried to find nth where N is out of index of the list"),
        new MtLo<Integer>(), "getListItem", 3)
        && t.checkExpect(makeList(34, 77, 90, 21, 67).getListItem(0), 34)
        && t.checkExpect(makeList(34, 77, 90, 21, 67).getListItem(2), 90)
        && t.checkException(
            new RuntimeException("Tried to find nth where N is out of index of the list"),
            makeList(34, 77, 90, 21, 67), "getListItem", 5);

  }

  // tests if two lists are the same
  boolean testSameList(Tester t) {
    return t.checkExpect(new MtLo<String>().sameList(new MtLo<String>()), true)
        && t.checkExpect(new MtLo<String>().sameList(makeList("hi", "yep", "cool", "hmm", "okay")),
            false)
        && t.checkExpect(makeList("a", "b", "c", "d", "e").sameList(new MtLo<String>()), false)
        && t.checkExpect(
            makeList("a", "b", "c", "d", "e").sameList(makeList("a", "b", "c", "d", "e")
                .append(new ConsLo<String>("happy", new MtLo<String>()))),
            false)
        && t.checkExpect(
            makeList("a", "b", "c", "d", "e").sameList(makeList("e", "c", "f", "z", "l")), false)
        && t.checkExpect(
            makeList("a", "b", "c", "d", "e").sameList(makeList("a", "b", "c", "d", "e")), true);
  }

  // tests if a list is the same as an empty list
  boolean testSameMt(Tester t) {
    return t.checkExpect(new MtLo<Integer>().sameMt(new MtLo<Integer>()), true)
        && t.checkExpect(makeList(7, 14, 21, 28, 35).sameMt(new MtLo<Integer>()), false);
  }

  // tests if a list is the same as a cons list
  boolean testSameCons(Tester t) {
    return t.checkExpect(
        makeList(2, 5, 8, 11, 14).sameCons(new ConsLo<Integer>(17, new MtLo<Integer>())), false)
        && t.checkExpect(new MtLo<Integer>().sameCons(makeList(4, 8, 12, 16, 20)), false)
        && t.checkExpect(makeList(8, 16, 24, 32, 40).sameCons(makeList(9, 16, 24, 45, 54)), false)
        && t.checkExpect(makeList(8, 16, 24, 32, 40).sameCons(makeList(8, 16, 24, 32, 40)), true);
  }

  // tests if a list contains a particular item
  boolean testContains(Tester t) {
    return t.checkExpect(new MtLo<String>().contains("hello"), false)
        && t.checkExpect(makeList("hi", "there", "how", "are", "you").contains("hello"), false)
        && t.checkExpect(makeList("well", "hello", "how", "are", "you").contains("hello"), true)
        && t.checkExpect(makeList("hello", "there", "how", "are", "you").contains("hello"), true);
  }

  // tests the appending of two lists
  boolean testAppend(Tester t) {
    return t.checkExpect(new MtLo<Integer>().append(mtLInt), mtLInt)
        && t.checkExpect(makeList(3, 7, 9, 100, 43).append(mtLInt), makeList(3, 7, 9, 100, 43))
        && t.checkExpect(
            new ConsLo<Integer>(8, new ConsLo<Integer>(9, mtLInt))
                .append(new ConsLo<Integer>(7, mtLInt)),
            new ConsLo<Integer>(8, new ConsLo<Integer>(9, new ConsLo<Integer>(7, mtLInt))));
  }

  Wordle theWordleInit = new Wordle(secretWord, 0, emptyLetterBlock, mtLString, wordListLength5);
  // Initial world

  Wordle theWordleInit2 = new Wordle(guessOfHello, 0, emptyLetterBlock, mtLString, wordListLength5);
  // Initial world2 for dordle

  Dordle dordleInit = new Dordle(theWordleInit, theWordleInit2); // An initial dordle

  // tests the reversal of a list
  boolean testReverse(Tester t) {
    return t.checkExpect(mtLInt.reverse(), mtLInt)
        && t.checkExpect(new ConsLo<Integer>(89, mtLInt).reverse(), new ConsLo<Integer>(89, mtLInt))
        && t.checkExpect(makeList(1, 2, 3, 4, 5).reverse(), makeList(5, 4, 3, 2, 1));
  }

  // tests the removal of the first item of a list, given that the first item
  // exists
  boolean testFirstRemoval(Tester t) {
    return t.checkExpect(mtLInt.removeFirst(), mtLInt)
        && t.checkExpect(new ConsLo<Integer>(89, mtLInt).removeFirst(), mtLInt)
        && t.checkExpect(makeList(1, 2, 3, 4, 5).removeFirst(), new ConsLo<Integer>(2,
            new ConsLo<Integer>(3, new ConsLo<Integer>(4, new ConsLo<Integer>(5, mtLInt)))));
  }

  Wordle thetheWordleInit = new Wordle(secretWord, 0, emptyLetterBlock, mtLString, wordListLength5);
  // Initial World

  // tests when a new key is pressed
  boolean testValidKey(Tester t) {
    return t.checkExpect(thetheWordleInit.onKeyEvent("shift"), thetheWordleInit)
        && t.checkExpect(thetheWordleInit.onKeyEvent("a"),
            new Wordle(secretWord, 0, emptyLetterBlock, new ConsLo<String>("A", new MtLo<String>()),
                wordListLength5))
        && t.checkExpect(new Wordle(secretWord, 0, emptyLetterBlock,
            new ConsLo<String>("a", new MtLo<String>()), wordListLength5).onKeyEvent("backspace"),
            thetheWordleInit)
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfOuter, wordListLength5)
                .onKeyEvent("enter"),
            new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(outerGuess, emptyLetterBlock),
                mtLString, wordListLength5));
  }

  // tests the commiting of a new guess
  boolean testCommiting(Tester t) {
    return t.checkExpect(
        new Wordle(secretWord, 0, emptyLetterBlock, guessOfWorld, wordListLength5).onCommitGuess(),
        new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(worldGuess, emptyLetterBlock),
            mtLString, wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfAAAAH, wordListLength5)
                .onCommitGuess(),
            new Wordle(secretWord, 0, emptyLetterBlock, mtLString, wordListLength5).onCommitGuess())
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, mtLString, wordListLength5).onCommitGuess(),
            new Wordle(secretWord, 0, emptyLetterBlock, mtLString, wordListLength5).onCommitGuess())
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, new ConsLo<String>("a", mtLString),
                wordListLength5).onCommitGuess(),
            new Wordle(secretWord, 0, emptyLetterBlock, new ConsLo<String>("a", mtLString),
                wordListLength5).onCommitGuess()); // guess is too short
  }

  // tests trying to add a new letter
  boolean testValidLetter(Tester t) {
    return t.checkExpect(
        new Wordle(secretWord, 0, emptyLetterBlock, new ConsLo<String>("A", new MtLo<String>()),
            wordListLength5).onValidLetter("b"),
        new Wordle(secretWord, 0, emptyLetterBlock,
            new ConsLo<String>("A", new ConsLo<String>("B", new MtLo<String>())), wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfAAAAH, wordListLength5)
                .onValidLetter("c"),
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfAAAAH, wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfAAAAH, wordListLength5)
                .onValidLetter("2"),
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfAAAAH, wordListLength5));
  }

  // tests trying to delete a letter
  boolean testDeleteLetter(Tester t) {
    return t.checkExpect(
        new Wordle(secretWord, 0, emptyLetterBlock, guessOfOuter, wordListLength5).onDeleteLetter(),
        new Wordle(secretWord, 0, emptyLetterBlock, wl.convertWordToList("OUTE"), wordListLength5))
        && t.checkExpect(theWordleInit.onDeleteLetter(), theWordleInit)
        && t.checkExpect(new Wordle(secretWord, 0, emptyLetterBlock,
            new ConsLo<String>("d", new MtLo<String>()), wordListLength5).onDeleteLetter(),
            theWordleInit);
  }

  // tests converting from a word to a list
  boolean testConverstion(Tester t) {
    return t.checkExpect(wl.convertWordToList("a"), new ConsLo<String>("a", new MtLo<String>()))
        && t.checkExpect(wl.convertWordToList("separate"),
            new ConsLo<String>("s",
                new ConsLo<String>("e", new ConsLo<String>("p",
                    new ConsLo<String>("a", new ConsLo<String>("r", new ConsLo<String>("a",
                        new ConsLo<String>("t", new ConsLo<String>("e", new MtLo<String>())))))))));
  }

  // tests the concatenation of two strings
  boolean testConcatenateString(Tester t) {
    return t.checkExpect(new ConcatenateString().apply("hello", "world"), "helloworld")
        && t.checkExpect(
            new ConcatenateString().apply(new ConcatenateString().apply("why", "hello"), "world"),
            "whyhelloworld")
        && t.checkExpect(
            new ConcatenateString().apply(new ConcatenateString().apply("why", "hello"),
                new ConcatenateString().apply("there", "buddy")),
            "whyhellotherebuddy");
  }

  // tests whether the game was won or not
  // 3 ways in order
  // 1. Correct on the last guess
  // 2. Correct before last guess
  // 3. Maxed out guesses but still incorrect
  boolean testWin(Tester t) {
    return t
        .checkExpect(
            new Wordle(secretWord, 6,
                new ConsLo<IList<LetterBlock>>(canoeGuess,
                    makeList(queenGuess, helloGuess, worldGuess, birdsGuess, oceanGuess)),
                mtLString, wordListLength5).win(),
            true)
        && t.checkExpect(new Wordle(secretWord, 3,
            new ConsLo<IList<LetterBlock>>(canoeGuess,
                new ConsLo<IList<LetterBlock>>(birdsGuess,
                    new ConsLo<IList<LetterBlock>>(oceanGuess, emptyLetterBlock))),
            mtLString, wordListLength5).win(), true)
        && t.checkExpect(new Wordle(secretWord, 6,
            new ConsLo<IList<LetterBlock>>(canoeGuess,
                makeList(queenGuess, helloGuess, worldGuess, birdsGuess, outerGuess)),
            mtLString, wordListLength5).win(), false);
  }

  // tests whether the game was lost or not
  // the reverse of the win() method, so we will use the same examples
  boolean testLose(Tester t) {
    return t
        .checkExpect(
            new Wordle(secretWord, 6,
                new ConsLo<IList<LetterBlock>>(canoeGuess,
                    makeList(queenGuess, helloGuess, worldGuess, birdsGuess, oceanGuess)),
                mtLString, wordListLength5).lose(),
            false)
        && t.checkExpect(new Wordle(secretWord, 3,
            new ConsLo<IList<LetterBlock>>(canoeGuess,
                new ConsLo<IList<LetterBlock>>(birdsGuess,
                    new ConsLo<IList<LetterBlock>>(oceanGuess, emptyLetterBlock))),
            mtLString, wordListLength5).lose(), false)
        && t.checkExpect(new Wordle(secretWord, 6,
            new ConsLo<IList<LetterBlock>>(canoeGuess,
                makeList(queenGuess, helloGuess, worldGuess, birdsGuess, outerGuess)),
            mtLString, wordListLength5).lose(), true);
  }

  // tests whether the game is over or not --> same results for shouldWorldEnd()
  // so need to retest
  boolean testGameOver(Tester t) {
    return t
        .checkExpect(new Wordle(secretWord, 6,
            new ConsLo<IList<LetterBlock>>(canoeGuess,
                makeList(queenGuess, helloGuess, worldGuess, birdsGuess, oceanGuess)),
            mtLString, wordListLength5).isGameOver(), true)
        && t.checkExpect(new Wordle(secretWord, 3,
            new ConsLo<IList<LetterBlock>>(canoeGuess,
                new ConsLo<IList<LetterBlock>>(birdsGuess,
                    new ConsLo<IList<LetterBlock>>(oceanGuess, emptyLetterBlock))),
            mtLString, wordListLength5).isGameOver(), true)
        && t.checkExpect(new Wordle(secretWord, 6,
            new ConsLo<IList<LetterBlock>>(canoeGuess, makeList(queenGuess, helloGuess, worldGuess,
                birdsGuess, outerGuess)),
            mtLString, wordListLength5).isGameOver(), true)
        && t.checkExpect(theWordleInit.isGameOver(), false)
        && t.checkExpect(
            new Wordle(secretWord, 3, new ConsLo<IList<LetterBlock>>(oceanGuess, emptyLetterBlock),
                mtLString, wordListLength5).isGameOver(),
            true)
        && t.checkExpect(new Wordle(secretWord, 3,
            new ConsLo<IList<LetterBlock>>(canoeGuess,
                new ConsLo<IList<LetterBlock>>(birdsGuess,
                    new ConsLo<IList<LetterBlock>>(queenGuess, emptyLetterBlock))),
            mtLString, wordListLength5).isGameOver(), false);
  }

  // compares guesses against a secret code thru
  // onCommitGuess() since onCommitGuess() is wahat compares a guess
  // to the secret code.
  boolean testComparison(Tester t) {
    return t.checkExpect(
        new Wordle(secretWord, 0, emptyLetterBlock, guessOfCanoe, wordListLength5).onCommitGuess(),
        new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(canoeGuess, emptyLetterBlock),
            mtLString, wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfBirds, wordListLength5)
                .onCommitGuess(),
            new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(birdsGuess, emptyLetterBlock),
                mtLString, wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, secretWord, wordListLength5)
                .onCommitGuess(),
            new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(oceanGuess, emptyLetterBlock),
                mtLString, wordListLength5))
        && t.checkExpect(
            new Wordle(wl.convertWordToList("LOAD"), 0, emptyLetterBlock, guessOfLoan,
                wordListLength4).onCommitGuess(),
            new Wordle(wl.convertWordToList("LOAD"), 1,
                new ConsLo<IList<LetterBlock>>(loanGuess, emptyLetterBlock), mtLString,
                wordListLength4))
        && t.checkExpect(
            new Wordle(wl.convertWordToList("GOODS"), 0, emptyLetterBlock, guessOfBlood,
                wordListLength5).onCommitGuess(),
            new Wordle(wl.convertWordToList("GOODS"), 1,
                new ConsLo<IList<LetterBlock>>(bloodGuess, emptyLetterBlock), mtLString,
                wordListLength5))
        && t.checkExpect(
            new Wordle(wl.convertWordToList("EARED"), 0, emptyLetterBlock, guessOfThere,
                wordListLength5).onCommitGuess(),
            new Wordle(wl.convertWordToList("EARED"), 1,
                new ConsLo<IList<LetterBlock>>(thereGuess, emptyLetterBlock), mtLString,
                wordListLength5))
        && t.checkExpect(
            new Wordle(wl.convertWordToList("GOODS"), 0, emptyLetterBlock, guessOfBooks,
                wordListLength5).onCommitGuess(),
            new Wordle(wl.convertWordToList("GOODS"), 1,
                new ConsLo<IList<LetterBlock>>(booksGuess, emptyLetterBlock), mtLString,
                wordListLength5))
        && t.checkExpect(
            new Wordle(wl.convertWordToList("START"), 0, emptyLetterBlock, guessOfTarts,
                wordListLength5).onCommitGuess(),
            new Wordle(wl.convertWordToList("START"), 1,
                new ConsLo<IList<LetterBlock>>(tartsGuess, emptyLetterBlock), mtLString,
                wordListLength5))
        && t.checkExpect(
            new Wordle(wl.convertWordToList("WALLS"), 0, emptyLetterBlock, guessOfError,
                wordListLength5).onCommitGuess(),
            new Wordle(wl.convertWordToList("WALLS"), 1,
                new ConsLo<IList<LetterBlock>>(errorGuess, emptyLetterBlock), mtLString,
                wordListLength5))
        && t.checkExpect(
            new Wordle(wl.convertWordToList("WALLS"), 0, emptyLetterBlock, guessOfWalls,
                wordListLength5).onCommitGuess(),
            new Wordle(wl.convertWordToList("WALLS"), 1,
                new ConsLo<IList<LetterBlock>>(wallsGuess, emptyLetterBlock), mtLString,
                wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfOuter, wordListLength5)
                .onCommitGuess(),
            new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(outerGuess, emptyLetterBlock),
                mtLString, wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfHello, wordListLength5)
                .onCommitGuess(),
            new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(helloGuess, emptyLetterBlock),
                mtLString, wordListLength5))
        && t.checkExpect(
            new Wordle(secretWord, 0, emptyLetterBlock, guessOfGlean, wordListLength5)
                .onCommitGuess(),
            new Wordle(secretWord, 1, new ConsLo<IList<LetterBlock>>(gleanGuess, emptyLetterBlock),
                mtLString, wordListLength5));
  }

  IList<Integer> testFoldIntList = new ConsLo<Integer>(3,
      new ConsLo<Integer>(10, new ConsLo<Integer>(30, new MtLo<Integer>())));
  IList<String> testFoldStringList = wl.convertWordToList("CANOE");

  // Tests the fold functions
  public boolean testFold(Tester t) {
    return t.checkExpect(testFoldIntList.foldl(new AddFoldFunc(), 0), 43) // cons foldl
        && t.checkExpect(testFoldStringList.foldl(new ConcatenateString(), ""), "EONAC")
        // cons fold

        && t.checkExpect(testFoldStringList.foldr(new ConcatenateString(), ""), "CANOE")
        // cons foldr

        // foldl on empty should return starting value
        && t.checkExpect(mtLInt.foldl(new AddFoldFunc(), 20139), 20139)
        && t.checkExpect(mtLInt.foldr(new AddFoldFunc(), 1482), 1482);
  }

  IList<LetterBlock> foldlCounterExample = this.secretWord
      .foldlWithCounter(new CompareGuess("OCEAN"), 0, new MtLo<LetterBlock>()).reverse();
  // Compares OCEAN with OCEAN in foldlCounterFunction

  IList<LetterBlock> foldlCounterAnswer = makeList(new LetterBlock("O", 0), new LetterBlock("C", 0),
      new LetterBlock("E", 0), new LetterBlock("A", 0), new LetterBlock("N", 0));

  // Tests the fold function with counter
  public boolean testFoldWCounter(Tester t) {
    return t.checkExpect(foldlCounterExample, foldlCounterAnswer) // Cons fold
        && t.checkExpect(mtLString.foldlWithCounter(new CompareGuess("OCEAN"), 0, queenOnOcean),
            queenOnOcean); // empty fold
  }

  // Wordle(int numOfGuesses, IList<IList<LetterBlock>> guessLetterBlocks,
  // IList<String> typingText, IList<String> wl)

  Dordle randomDord = new Dordle(new Wordle(0, emptyLetterBlock, mtLString, wordListLength5),
      new Wordle(0, emptyLetterBlock, mtLString, wordListLength5));

  Dordle winBoth = new Dordle(
      new Wordle(secretWord, 6,
          new ConsLo<IList<LetterBlock>>(bloodGuess,
              makeList(queenGuess, helloGuess, worldGuess, birdsGuess, oceanGuess)),
          mtLString, wordListLength5),
      (new Wordle(guessOfWalls, 1, new ConsLo<IList<LetterBlock>>(wallsGuess, emptyLetterBlock),
          mtLString, wordListLength5)));
  // win the first wordle in the 6th try and win the second one in the first try

  Dordle loseFirst = new Dordle(
      new Wordle(secretWord, 6,
          new ConsLo<IList<LetterBlock>>(bloodGuess,
              makeList(queenGuess, helloGuess, worldGuess, birdsGuess, canoeGuess)),
          mtLString, wordListLength5),
      (new Wordle(guessOfWalls, 6, new ConsLo<IList<LetterBlock>>(wallsGuess, emptyLetterBlock),
          mtLString, wordListLength5))); 
  // lose the first wordle but win the second one in the first try
                                         

  Dordle loseSecond = new Dordle(
      new Wordle(secretWord, 6,
          new ConsLo<IList<LetterBlock>>(bloodGuess,
              makeList(queenGuess, helloGuess, worldGuess, birdsGuess, oceanGuess)),
          mtLString, wordListLength5),
      (new Wordle(guessOfTarts, 6,
          new ConsLo<IList<LetterBlock>>(bloodGuess,
              makeList(queenGuess, helloGuess, worldGuess, birdsGuess, canoeGuess)),
          mtLString, wordListLength5))); 
  // win the first wordle in the third try but lose the second one
                                      

  Dordle loseBoth = new Dordle(
      new Wordle(secretWord, 6,
          new ConsLo<IList<LetterBlock>>(bloodGuess,
              makeList(queenGuess, helloGuess, worldGuess, birdsGuess, canoeGuess)),
          mtLString, wordListLength5),
      (new Wordle(secretWord, 6,
          new ConsLo<IList<LetterBlock>>(bloodGuess,
              makeList(queenGuess, helloGuess, worldGuess, birdsGuess, canoeGuess)),
          mtLString, wordListLength5))); // lose both wordles

  // tests the conditions where a dordle wins
  // only wins if both wordles are won
  boolean testDordleWin(Tester t) {
    return t.checkExpect(winBoth.win(), true) && t.checkExpect(loseFirst.win(), false)
        && t.checkExpect(loseSecond.win(), false) && t.checkExpect(loseBoth.win(), false);
  }

  //Examples for testing rendering
  IList<WorldImage> mtWorldImage = new MtLo<WorldImage>();
  IList<String> stringListSPA = wl.convertWordToList("SPA");
  Wordle typingSPA = new Wordle(0, emptyLetterBlock, stringListSPA, wordListLength5); 
  // initial world

  IList<TypingBlock> typingBlocksSPA = this.stringListSPA.map(new StringToTypingBlock());
  IList<WorldImage> typingBlockImageListSPA = typingBlocksSPA.map(new TypingBlockToImage());

  //Rendering bigger test methods, checks to make sure calls down accordingly
  public boolean testBiggerRendering(Tester t) {
    return t.checkExpect(theWordleInit.drawScene(),
        new AboveImage(theWordleInit.drawAllCommitedGuesses(), theWordleInit.drawTypingBlocks()))
        && t.checkExpect(theWordleInit.drawAllCommitedGuesses(),
            theWordleInit.guessLetterBlocks.foldl(new AboveSingleGuessFunc(), new EmptyImage()))
        && t.checkExpect(theWordleInit.drawTypingBlocks(),
            mtWorldImage.foldl(new BesideFoldFunc(), new EmptyImage()))
        && t.checkExpect(typingSPA.drawTypingBlocks(),
            typingBlockImageListSPA.foldl(new BesideFoldFunc(), new EmptyImage()));
  }

  // Eventually runs big-bang replace null by final world definition
  boolean testBigBang(Tester t) {
    Dordle ww = randomDord; // INIT WORLD STATE HERE (either theWordleInit or dordleInit)
    int worldWidth = SCREEN_WIDTH;
    int worldHeight = SCREEN_HEIGHT;
    double tickRate = 20;
    return ww.bigBang(worldWidth, worldHeight, tickRate);
  }

**/
}