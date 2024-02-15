package com.teamhide.kream.product.adapter.out.persistence.jpa

import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.product.makeProductBrand
import com.teamhide.kream.product.makeProductCategory
import com.teamhide.kream.support.test.JpaRepositoryTest
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@JpaRepositoryTest
internal class ProductQuerydslRepositoryImplTest(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val productBrandRepository: ProductBrandRepository,
) {
    @Test
    fun `id로 ProductInfoDto를 조회한다`() {
        // Given
        val productCategory = makeProductCategory()
        productCategoryRepository.save(productCategory)

        val productBrand = makeProductBrand()
        productBrandRepository.save(productBrand)

        val product = makeProduct(productCategory = productCategory, productBrand = productBrand)
        productRepository.save(product)

        val user = makeUser()
        userRepository.save(user)

        // When
        val sut = productRepository.findInfoById(productId = product.id)

        // Then
        sut.shouldNotBeNull()
        sut.productId shouldBe product.id
        sut.releasePrice shouldBe product.releasePrice
        sut.modelNumber shouldBe product.modelNumber
        sut.name shouldBe product.name
        sut.brand shouldBe productBrand.name
        sut.category shouldBe productCategory.name
    }
}
