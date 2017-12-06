package com.kdx.loho.collectionexample;


import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;package java.util;

import java.util.function.Consumer;

import static android.R.attr.x;


public class LinkedList<E>
        extends AbstractSequentialList<E>
        implements List<E>, Deque<E>, Cloneable,
        java.io.Serializable {
    //LinkedList中链表元素个数
    transient int size = 0;
    //链表头结点
    transient Node<E> first;
    //链表尾结点
    transient Node<E> last;

    //默认构造方法，生成一个空的链表
    public LinkedList() {
    }

    //根据c里面的元素生成一个LinkedList
    public LinkedList(Collection<? extends E> c) {
        //调用空的构造方法
        this();
        //将c里面的元素添加到空链表尾部
        addAll(c);
    }

    //首部增加结点，结点的值为e
    private void linkFirst(E e) {

        final Node<E> f = first;//f指向头结点
        //生成一个新结点，结点的值为e，其前向指针为null，后向指针为f
        final Node<E> newNode = new Node<>(null, e, f);
        //first指向新生成的结点，f保存着旧的头结点信息
        first = newNode;
        if (f == null)
            //如果f为null,则表示整个链表目前是空的，则尾结点也指向新结点
            last = newNode;
        else
            //f(老的头结点)的前向指向最新的结点信息
            f.prev = newNode;
        size++;//元素个数+1
        modCount++;//修改次数+1
    }

    //尾部增加结点，结点的值为e
    void linkLast(E e) {
        final Node<E> l = last; //l指向尾结点
        //生成一个新结点，结点的值为e，其前向指针为l，后向指针为null
        final Node<E> newNode = new Node<>(l, e, null);
        //last指向新生成的结点，l保存着旧的尾结点信息
        last = newNode;
        if (l == null)
            //如果l为null，则表示整个链表目前是空的，则头结点也指向新结点
            first = newNode;
        else
            //l(旧的尾结点)的后向指针指向最新的结点信息
            l.next = newNode;
        size++;//元素个数+1
        modCount++;//修改次数+1
    }

    //非空结点succ之前插入新结点，新结点的值为e
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null; //外界调用需保证succ不为null,否则程序会抛出空指针异常
        final Node<E> pred = succ.prev;//pred指向succ的前向结点
        //生成一个新结点，结点的值为e，其前向指针指向pred,后向指针指向succ
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;//succ的前向指针指向newNode
        if (pred == null)
            //如果pred为null,则表示succ为头结点，此时头结点指向最新生成的结点newNode
            first = newNode;
        else
            //pred的后向指针指向新生成的结点，此时已经完成了结点的插入操作
            pred.next = newNode;
        size++;//元素个数+1
        modCount++;//修改次数+1
    }

    //删除头结点，并返回该结点的值
    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;//需确保f为头结点，且链表不为null
        final E element = f.item;//获得结点的值
        final Node<E> next = f.next;//next指向f的后向结点
        f.item = null;//释放数据结点
        f.next = null; // help GC   //释放f的后向指针
        first = next;   //first指向f的后向结点
        if (next == null)
            //如果next为null,则表示f为last结点，此时链表即为空链表
            last = null;
        else
            //修改next的前向指针，因为first结点的前向指针为null
            next.prev = null;
        size--; //元素个数-1
        modCount++; //修改次数+1
        return element;
    }

    //删除尾结点，并返回尾结点的内容
    private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;   //需确保l为尾结点，且链表不为null
        final E element = l.item;   //获得结点的值
        final Node<E> prev = l.prev;    //prev执行1的前向结点
        l.item = null;  //释放l结点的值
        l.prev = null; // help GC   //释放l结点的前向指针
        last = prev;    //last结点指向l的前向结点
        if (prev == null)
            //如果prev为null,则表示l为first结点，此时链表即为空链表
            first = null;
        else
            //修改prev的后向指针，因为last结点的后向指针为null
            prev.next = null;
        size--;//元素个数-1
        modCount++;//修改次数+1
        return element;
    }

    //删除结点x
    E unlink(Node<E> x) {
        // assert x != null;    //需确保x不为null，否则后续操作会抛出空指针异常
        final E element = x.item;   //保存x结点的值
        final Node<E> next = x.next;//next指向x的后向结点
        final Node<E> prev = x.prev;//prev指向x的前向结点

        if (prev == null) {
            //如果prev为空，则x结点为first结点，此时first结点指向next结点（x的后向结点）
            first = next;
        } else {
            prev.next = next;//x的前向结点的后向指针指向x的后向结点
            x.prev = null;  //释放x的前向指针
        }

        if (next == null) {
            //如果next结点为空，则x结点为尾部结点，此时last结点指向prev结点（x的前向结点）
            last = prev;
        } else {
            next.prev = prev;//x的后向结点的前向指针指向x的前向结点
            x.next = null;  //释放x的后向指针
        }

        x.item = null;  //释放x的值节点，此时x节点可以完全被GC回收
        size--; //元素个数-1
        modCount++; //修改次数+1
        return element;
    }

    //获得头结点的值
    public E getFirst() {
        final Node<E> f = first;//f指向first结点
        if (f == null)//如果链表为空
            throw new NoSuchElementException();
        return f.item;//返回first结点的值
    }

    //获得尾结点的值
    public E getLast() {
        final Node<E> l = last; //l指向last结点
        if (l == null)//如果链表为空
            throw new NoSuchElementException();
        return l.item;//返回last结点的值
    }

    //移除头结点
    public E removeFirst() {
        final Node<E> f = first;//获得头结点
        if (f == null)//如果链表为空
            throw new NoSuchElementException();
        return unlinkFirst(f);  //摘除头结点
    }

    //移除尾结点
    public E removeLast() {
        final Node<E> l = last;//获得尾结点
        if (l == null)//如果链表为空
            throw new NoSuchElementException();
        return unlinkLast(l);//摘除尾结点
    }

    //添加到头结点，结点的值为e
    public void addFirst(E e) {
        linkFirst(e);//添加到头部
    }

    //添加到尾结点
    public void addLast(E e) {
        linkLast(e);//添加到尾部
    }

    //判断元素（值为o）是否o在链表中
    public boolean contains(Object o) {
        return indexOf(o) != -1;//定位元素
    }

    //返回元素个数
    public int size() {
        return size;
    }

    //添加元素，元素值为e
    public boolean add(E e) {
        linkLast(e);//添加到链表尾部
        return true;
    }

    //移除值为o的元素，o可以为null，找到一个删除即返回
    public boolean remove(Object o) {
        if (o == null) {//元素为null
            for (Node<E> x = first; x != null; x = x.next) {//从结点开始遍历
                if (x.item == null) {//找到一个结点
                    unlink(x);  //删除元素
                    return true;
                }
            }
        } else {//元素不为空
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    //将c中的元素都添加到当前链表中
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);//添加到链表尾部
    }

    //在序号为index处，添加c中所有的元素到当前链表中（后向添加）
    public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index);//判断index是否超出界

        Object[] a = c.toArray();//将集合转换为数组
        int numNew = a.length;
        if (numNew == 0)
            return false;

        Node<E> pred, succ;
        if (index == size) {//如果index为元素个数，即index个结点为尾结点
            succ = null;
            pred = last;//指向为结点
        } else {
            succ = node(index); //succ指向第index个结点
            pred = succ.prev;   //pred指向succ的前向结点
        }

        //for循环结束后，a里面的元素都添加到当前链表里面，后向添加
        for (Object o : a) {
            @SuppressWarnings("unchecked") E e = (E) o;
            //新生成一个结点，结点的前向指针指向pred,后向指针为null
            Node<E> newNode = new Node<>(pred, e, null);
            if (pred == null)
                //如果pred为null，则succ为当前头结点
                first = newNode;
            else
                //pred的后向指针指向新结点
                pred.next = newNode;
            pred = newNode;//pred移动到新结点
        }

        if (succ == null) {
            last = pred;//succ为null，这表示index为尾结点之后
        } else {
            //pred表示所有元素添加之后的最后结点，此时pred的后向指针指向之前的记录的结点
            pred.next = succ;
            succ.prev = pred;//之前记录的结点指向添加元素之后的最后结点
        }

        size += numNew;//元素个数+num
        modCount++;//修改次数+1
        return true;
    }

    //清除链表里面的所有元素
    public void clear() {
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;  //释放值结点，便于GC回收
            x.next = null;  //释放前向指针
            x.prev = null;  //释放后向指针
            x = next;   //后向遍历
        }
        first = last = null;//释放头尾结点
        size = 0;
        modCount++;
    }


    //获得第index个结点的值
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;    //点位第index结点，返回值信息
    }

    //设置第index元素的值
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);//定位第index个结点
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    //第index个结点之前添加结点
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    //删除第index个结点
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    //判断index是否是链表中的元素的索引
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    //判断index是否是链表中的元素的索引
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    //定位链表中的第index个结点
    Node<E> node(int index) {
        // assert isElementIndex(index);//确保是合法的索引，即0<=index<=size
        //index小于size的一半时，从头向后找
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {//index大于size的一半时，从尾向前找
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    //定位元素，首次出现的元素的值为o的结点序号
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    //定位元素，最后一次出现的元素值为o的元素序号
    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (x.item == null)
                    return index;
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.item))
                    return index;
            }
        }
        return -1;
    }

    //实现队列操作，返回第一个元素的值
    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    //实现队列操作，返回第一个结点
    public E element() {
        return getFirst();
    }

    //实现队列操作，弹出第一个结点
    public E poll() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    //删除结点
    public E remove() {
        return removeFirst();
    }

    //添加结点
    public boolean offer(E e) {
        return add(e);
    }

    //添加头结点
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    //添加尾结点
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    //返回头结点的值
    public E peekFirst() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    //返回尾结点的值
    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.item;
    }

    //弹出第一个结点
    public E pollFirst() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    //弹出最后一个结点
    public E pollLast() {
        final Node<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }

    //添加头部结点
    public void push(E e) {
        addFirst(e);
    }

    //弹出第一个结点
    public E pop() {
        return removeFirst();
    }

    //删除值为o的结点
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    //删除值为o的结点（从尾部遍历）
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    //返回双向迭代器
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index);
        return new ListItr(index);
    }

    private class ListItr implements ListIterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;
        private int expectedModCount = modCount;

        ListItr(int index) {
            // assert isPositionIndex(index);
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        public boolean hasNext() {
            return nextIndex < size;
        }

        public E next() {
            checkForComodification();
            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        public E previous() {
            checkForComodification();
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next = (next == null) ? last : next.prev;
            nextIndex--;
            return lastReturned.item;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            checkForComodification();
            if (lastReturned == null)
                throw new IllegalStateException();

            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        public void set(E e) {
            if (lastReturned == null)
                throw new IllegalStateException();
            checkForComodification();
            lastReturned.item = e;
        }

        public void add(E e) {
            checkForComodification();
            lastReturned = null;
            if (next == null)
                linkLast(e);
            else
                linkBefore(e, next);
            nextIndex++;
            expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (modCount == expectedModCount && nextIndex < size) {
                action.accept(next.item);
                lastReturned = next;
                next = next.next;
                nextIndex++;
            }
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private static class Node<E> {
        E item; //结点的值
        Node<E> next;   //结点的后向指针
        Node<E> prev;   //结点的前向指针

        //构造方法中已完成Node成员的赋值
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;    //结点的值赋值为element
            this.next = next;       //后向指针赋值
            this.prev = prev;       //前向指针赋值
        }
    }

    //返回前向迭代器
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    //前向迭代器
    private class DescendingIterator implements Iterator<E> {
        private final ListItr itr = new ListItr(size());

        public boolean hasNext() {
            return itr.hasPrevious();
        }

        public E next() {
            return itr.previous();
        }

        public void remove() {
            itr.remove();
        }
    }

    @SuppressWarnings("unchecked")
    private LinkedList<E> superClone() {
        try {
            return (LinkedList<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    //拷贝操作，执行浅拷贝，只复制引用，而没有复制引用指向的内存
    public Object clone() {
        LinkedList<E> clone = superClone();

        // Put clone into "virgin" state
        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;

        // Initialize clone with our elements
        for (Node<E> x = first; x != null; x = x.next)
            clone.add(x.item);

        return clone;
    }

    //转换为数组
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    //转换为数组
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    //序列化版本
    private static final long serialVersionUID = 876323262645176354L;

    //序列化
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out size
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (Node<E> x = first; x != null; x = x.next)
            s.writeObject(x.item);
    }

    //反序列化
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // Read in size
        int size = s.readInt();

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++)
            linkLast((E) s.readObject());
    }

    //获取一个分割器，1.8新增
    @Override
    public Spliterator<E> spliterator() {
        return new LLSpliterator<E>(this, -1, 0);
    }

    /**
     * A customized variant of Spliterators.IteratorSpliterator
     */
    static final class LLSpliterator<E> implements Spliterator<E> {
        static final int BATCH_UNIT = 1 << 10;  // batch array size increment
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        final LinkedList<E> list; // null OK unless traversed
        Node<E> current;      // current node; null until initialized
        int est;              // size estimate; -1 until first needed
        int expectedModCount; // initialized when est set
        int batch;            // batch size for splits

        LLSpliterator(LinkedList<E> list, int est, int expectedModCount) {
            this.list = list;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEst() {
            int s; // force initialization
            final LinkedList<E> lst;
            if ((s = est) < 0) {
                if ((lst = list) == null)
                    s = est = 0;
                else {
                    expectedModCount = lst.modCount;
                    current = lst.first;
                    s = est = lst.size;
                }
            }
            return s;
        }

        public long estimateSize() {
            return (long) getEst();
        }

        public Spliterator<E> trySplit() {
            Node<E> p;
            int s = getEst();
            if (s > 1 && (p = current) != null) {
                int n = batch + BATCH_UNIT;
                if (n > s)
                    n = s;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do {
                    a[j++] = p.item;
                } while ((p = p.next) != null && j < n);
                current = p;
                batch = j;
                est = s - j;
                return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Node<E> p;
            int n;
            if (action == null) throw new NullPointerException();
            if ((n = getEst()) > 0 && (p = current) != null) {
                current = null;
                est = 0;
                do {
                    E e = p.item;
                    p = p.next;
                    action.accept(e);
                } while (p != null && --n > 0);
            }
            if (list.modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            Node<E> p;
            if (action == null) throw new NullPointerException();
            if (getEst() > 0 && (p = current) != null) {
                --est;
                E e = p.item;
                current = p.next;
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

}
