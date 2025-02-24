package ir.amirreza

import ir.amirreza.hibernate.Author
import ir.amirreza.hibernate.Post
import org.hibernate.SessionFactory

object HibernateUtil {
    val sessionFactory: SessionFactory = org.hibernate.cfg.Configuration()
        .configure()
        .addAnnotatedClass(Post::class.java)
        .addAnnotatedClass(Author::class.java)
        .buildSessionFactory()

    fun closeSession() {
        sessionFactory.close()
    }

}

fun getPosts(): List<Post> {
    val session = HibernateUtil.sessionFactory.openSession()
    val users = session.createQuery("FROM Post", Post::class.java).list()
    session.close()
    return users
}

fun addFakePosts() {
    val session = HibernateUtil.sessionFactory.openSession()
    val transaction = session.beginTransaction()

    try {
        val author = Author(name = "John Doe")
        session.persist(author)

        val post1 = Post(
            title = "Introduction to Ktor",
            content = "Ktor is a framework for building asynchronous servers and clients using Kotlin.",
            author = author
        )
        val post2 = Post(
            title = "Understanding Hibernate in Kotlin",
            content = "Hibernate is an ORM framework that simplifies database interactions in Kotlin applications.",
            author = author
        )

        session.persist(post1)
        session.persist(post2)

        transaction.commit()
        println("SUCCESS")
    } catch (e: Exception) {
        transaction.rollback()
        println("ERROR: ${e.message}")
        e.printStackTrace()
    } finally {
        session.close()
    }
}